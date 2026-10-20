import http from 'k6/http';
import exec from 'k6/execution';
import { check, fail } from 'k6';

const baseUrl = 'http://localhost:8080';
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

export function setup() {
    const product = http.post(
        `${baseUrl}/product`,
        JSON.stringify({
            name: `loadtest-${Date.now()}-${Math.random().toString(36).slice(2)}`,
            price: 1000,
        }),
        { headers }
    );

    if (!check(product, { 'product created': (r) => r.status === 200 })) {
        fail(`Product creation failed: ${product.status} ${product.body}`);
    }

    const productId = product.json();
    if (!Number.isSafeInteger(productId) || productId <= 0) {
        fail('Product creation did not return a valid product ID');
    }

    // The inventory API sets the available quantity to 1,000.
    const inventory = http.put(`${baseUrl}/inventories/${productId}`, null);
    if (!check(inventory, { 'inventory prepared': (r) => r.status === 200 })) {
        fail(`Inventory setup failed: ${inventory.status} ${inventory.body}`);
    }

    return { productId };
}

export default function ({ productId }) {
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
