import http from 'k6/http';
import exec from 'k6/execution';
import { check } from 'k6';

const baseUrl = __ENV.BASE_URL || 'http://localhost:8080';
const productId = Number(__ENV.PRODUCT_ID);

if (!Number.isSafeInteger(productId) || productId <= 0) {
    throw new Error('Set PRODUCT_ID to the prepared product ID');
}
const headers = { 'Content-Type': 'application/json' };
const purchaseLimit = 1000;

export const options = {
    scenarios: {
        purchase: {
            executor: 'constant-arrival-rate',
            rate: 1000,
            timeUnit: '1s',
            duration: '1s',
            preAllocatedVUs: 1000,
            maxVUs: 1000,
            gracefulStop: '30s',
        },
    },
    thresholds: {
        'http_reqs{scenario:purchase}': [`count==${purchaseLimit}`],
        dropped_iterations: ['count==0'],
        http_req_failed: ['rate==0'],
        checks: ['rate==1'],
    },
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

    check(response, {
        'purchase succeeded': (r) => r.status === 200,
    });
}
