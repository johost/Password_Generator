import { useState } from 'react';

export default function PasswordGenerator() {
    const [length, setLength] = useState(12);
    const [password, setPassword] = useState('');
    const [details, setDetails] = useState(null);

    const generate = async () => {
        try {
            const res = await fetch(`/api/password/details?length=${length}`); 
            if (!res.ok) throw new Error('Network error');
            const data = await res.json(); 
            setPassword(data.password); 
            setDetails(data); 
        } catch (err) {
            console.error(err);
            setPassword('Error generating password');
            setDetails(null); 
        }
    };

    const getStrengthLabel = (entropy) => {
        if (entropy < 40) return { label: 'Very weak', time: 'seconds', color: '#cc0000' };
        if (entropy < 60) return { label: 'Weak', time: 'minutes–hours', color: '#cc6600' };
        if (entropy < 80) return { label: 'Moderate', time: 'days–months', color: '#cccc00' };
        if (entropy < 100) return { label: 'Strong', time: 'years', color: '#008800' };
        return { label: 'Very strong', time: 'centuries+', color: '#006666' };
    };

    return (
        <div className="PW-component-wrapper">
            <div className="generate-PW-section">
                <h1>Generate Password</h1>
                <label htmlFor="length-input" className="length-label">
                    Password length:
                    <input
                        id="length-input"
                        type="number"
                        min="4"
                        max="64"
                        value={length}
                        onChange={e => setLength(Number(e.target.value))}
                        className="length-input"
                    />
                </label>
                <p>Length must be between 4-64 characters.</p>
                <button className="generate-button" onClick={generate}>Generate Password</button>
                <code className="password-output">{password}</code>
            </div>
            {details && (
                <div className="password-details">
                    <div className="strength-indicator">
                        <h3>Password Strength</h3>
                        {(() => {
                            const strength = getStrengthLabel(details.theoreticalEntropy);
                            return (
                                <div className="strength-content">
                                    <div>
                                        <p>Entropy: {details.entropy.toFixed(2)} bits</p>
                                        <p>Theoretical max: {details.theoreticalEntropy.toFixed(2)} bits</p>
                                        <p>Theoretical max strength: <span className="strength-label" style={{ color: strength.color }}>
                                             {strength.label} </span>({strength.time})
                                        </p>
                                    </div>
                                </div>
                            );
                        })()}
                    </div>

                    <div className="character-distribution">
                        <h3>Character Distribution</h3>
                        <div className="char-grid">
                            {Object.entries(details.frequencies).map(([char, freq]) => (
                                <div key={char} className="char-item">
                                    <div className="char-value">{char}</div>
                                    <div>Count: {freq}</div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}