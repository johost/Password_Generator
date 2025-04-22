// frontend/src/components/PasswordGenerator.jsx
import { useState } from 'react';

export default function PasswordGenerator() {
    const [length, setLength] = useState(12);
    const [password, setPassword] = useState('');

    const generate = async () => {
        try {
            const res = await fetch(`/api/password?length=${length}`);
            if (!res.ok) throw new Error('Network error');
            const pwd = await res.text();
            setPassword(pwd);
        } catch (err) {
            console.error(err);
            setPassword('Error generating password');
        }
    };

    return (
        <div class="PW-component-wrapper">
            <label htmlFor="length-input">
                Password length:
                <input
                    id="length-input"
                    type="number"
                    min="4"
                    max="64"
                    value={length}
                    onChange={e => setLength(Number(e.target.value))}
                    style={{ marginLeft: '0.5rem', width: '4rem' }}
                />
            </label>
            <p>Length must be between 4-64 characters.</p>
            <button class="generate-button" onClick={generate}>Generate Password</button>
            <code class="password-output">{password}</code>
        </div>
    );
}
