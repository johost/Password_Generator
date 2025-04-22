import { useState } from 'react'
import PasswordGenerator from './components/PasswordGenerator'

export default function App() {
  const [password, setPassword] = useState('')

  const generate = async () => {
    const response = await fetch('/api/password?length=12')
    const text = await response.text()
    setPassword(text)
  }

  return (
    <div>
      <PasswordGenerator />
    </div>
  )
}
