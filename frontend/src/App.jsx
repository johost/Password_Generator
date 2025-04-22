import { useState } from 'react'

export default function App() {
  const [password, setPassword] = useState('')

  const generate = async () => {
    const response = await fetch('/api/password?length=12')
    const text = await response.text()
    setPassword(text)
  }

  return (
    <div>
      <button onClick={generate}>Generate Password</button>
      <p>{password}</p>
    </div>
  )
}
