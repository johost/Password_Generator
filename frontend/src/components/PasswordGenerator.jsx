// frontend/src/components/PasswordGenerator.jsx
import { useState } from 'react';

export default function PasswordGenerator() {
  const [password, setPassword] = useState('');

  const generate = async () => {
    try {
      const res = await fetch(`/api/password?length=16`);
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
      <button class="generate-button" onClick={generate}>Generate Password</button>
      <code class="password-output">{password}</code>
    </div>
  );
}
