import http from 'k6/http';
import { check, fail } from 'k6';

const baseUrl = __ENV.BASE_URL || 'http://localhost:8080';

export const options = {
    vus: 1,
    iterations: 1,
    thresholds: {
        http_req_failed: ['rate==0'],
        checks: ['rate==1'],
    },
};

export default function () {
    const product = http.post(
        `${baseUrl}/product`,
        JSON.stringify({
            name: `loadtest-${Date.now()}-${Math.random().toString(36).slice(2)}`,
            price: 1000,
        }),
        { headers: { 'Content-Type': 'application/json' } }
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

    console.log(`Product ready with quantity 1000. Run: k6 run -e PRODUCT_ID=${productId} performance/scenarios/purchase.js`);
}
