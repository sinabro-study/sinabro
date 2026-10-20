import http from 'k6/http';
import exec from 'k6/execution';
import { check } from 'k6';

export const options = {
    scenarios: {
        seed_users: {
            executor: 'shared-iterations',
            vus: 20,
            iterations: 100000,
            maxDuration: '60m',
        },
    },
};

export default function () {
    const index = exec.scenario.iterationInTest;

    const response = http.post(
        `http://localhost:8080/member`,
        JSON.stringify({
            name: `loadtest-${index}`,
        }),
        {
            headers: {
                'Content-Type': 'application/json',
            },
        }
    );

    check(response, {
        'member registered': (r) => r.status === 200,
    });
}
