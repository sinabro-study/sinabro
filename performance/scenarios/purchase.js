import http from 'k6/http';
import exec from 'k6/execution';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

const baseUrl = __ENV.BASE_URL || 'http://localhost:8080';
const productId = Number(__ENV.PRODUCT_ID);

if (!Number.isSafeInteger(productId) || productId <= 0) {
    throw new Error('Set PRODUCT_ID to the prepared product ID');
}
const headers = { 'Content-Type': 'application/json' };
const purchaseLimit = 1000;
const duration = 10
const allocatedVUs = Number(__ENV.VUS || purchaseLimit);

if (!Number.isSafeInteger(allocatedVUs) || allocatedVUs <= 0) {
    throw new Error('VUS must be a positive integer');
}

const successes = new Counter('purchase_successes');
const soldOut = new Counter('purchase_sold_out');
const unexpected = new Counter('purchase_unexpected');

http.setResponseCallback(http.expectedStatuses(200, 422));

export const options = {
    scenarios: {
        purchase: {
            executor: 'constant-arrival-rate',
            rate: purchaseLimit / duration,
            timeUnit: '1s',
            duration: `${duration}s`,
            preAllocatedVUs: allocatedVUs,
            maxVUs: allocatedVUs,
            gracefulStop: '60s',
        },
    },
    thresholds: {
        'http_reqs{scenario:purchase}': [`count==${purchaseLimit}`],
        dropped_iterations: ['count==0'],
        http_req_failed: ['rate==0'],
        checks: ['rate==1'],
        purchase_successes: ['count==1000'],
        purchase_sold_out: [`count==${purchaseLimit - 1000}`],
        purchase_unexpected: ['count==0'],
    },
    summaryTrendStats: ['avg', 'med', 'p(90)', 'p(95)', 'p(99)', 'max'],
};

export default function () {
    const index = exec.scenario.iterationInTest;
    // Arrival-rate scheduling can start an extra iteration at the time boundary.
    if (index >= purchaseLimit) return;

    const response = http.post(
        `${baseUrl}/purchases`,
        JSON.stringify({
            memberId: index + 1,
            productId,
            quantity: 1,
        }),
        {
            headers,
            timeout: '30s',
        }
    );

    const succeeded = response.status === 200;
    let outOfStock = false;
    if (response.status === 422) {
        try {
            outOfStock = response.json('code') === 'OUT_OF_STOCK';
        } catch (_) {
            // Invalid JSON is an unexpected response.
        }
    }
    successes.add(succeeded ? 1 : 0);
    soldOut.add(outOfStock ? 1 : 0);
    unexpected.add(succeeded || outOfStock ? 0 : 1, {
        status: String(response.status),
        error_code: String(response.error_code || 0),
    });
    check(response, {
        'purchase succeeded or sold out': () => succeeded || outOfStock,
    });
}
