import { useState, useMemo } from 'react'
import { ResponsiveBar } from '@nivo/bar'

// Character set sizes (must match backend) as fallback
const DEFAULT_CHARSET_SIZE = 26 + 26 + 10 + 13  // lowercase + uppercase + digits + symbols

export default function PasswordGenerator() {
  const [length, setLength] = useState(12)
  const [password, setPassword] = useState('')
  const [details, setDetails] = useState(null)
  const [viewMode, setViewMode] = useState('list')

  const generate = async () => {
    try {
      const res = await fetch(`/api/password/details?length=${length}`)
      if (!res.ok) throw new Error('Network error')
      const data = await res.json()
      setPassword(data.password)
      setDetails(data)
    } catch (err) {
      console.error(err)
      setPassword('Error generating password')
      setDetails(null)
    }
  }

  const charsetSize = details?.characterSetSize || DEFAULT_CHARSET_SIZE
  // Recompute theoretical max entropy whenever length or charsetSize changes
  const theoreticalMax = useMemo(() => {
    return length * (Math.log(charsetSize) / Math.log(2))
  }, [length, charsetSize])

  const getStrengthLabel = (entropy) => {
    if (entropy < 40) return { label: 'Very weak', time: 'seconds', color: '#cc0000' }
    if (entropy < 60) return { label: 'Weak', time: 'minutes–hours', color: '#cc6600' }
    if (entropy < 80) return { label: 'Moderate', time: 'days–months', color: '#cccc00' }
    if (entropy < 100) return { label: 'Strong', time: 'years', color: '#008800' }
    return { label: 'Very strong', time: 'centuries+', color: '#006666' }
  }

  // Prepare sorted frequency array for bar chart
  const freqArray = useMemo(() => {
    if (!details?.frequencies) return []
    return Object.entries(details.frequencies)
      .map(([char, count]) => ({ char, count }))
      .sort((a, b) => b.count - a.count)
  }, [details])

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
            <div className="strength-content">
              <p>Current Entropy: {details.entropy.toFixed(2)} bits</p>
              <p>Theoretical max: {theoreticalMax.toFixed(2)} bits</p>
              <p>
                Theoretical max strength:{' '}
                <span className="strength-label" style={{ color: getStrengthLabel(theoreticalMax).color }}>
                  {getStrengthLabel(theoreticalMax).label}
                </span>{' '}
                ({getStrengthLabel(theoreticalMax).time})
              </p>
            </div>

            <div className="view-toggle">
              <button
                className={viewMode === 'list' ? 'active' : ''}
                onClick={() => setViewMode('list')}
              >
                List
              </button>
              <button
                className={viewMode === 'bar' ? 'active' : ''}
                onClick={() => setViewMode('bar')}
              >
                Bar Chart
              </button>
            </div>

            {viewMode === 'list' ? (
              <div className="char-grid">
                {Object.entries(details.frequencies).map(([char, freq]) => (
                  <div key={char} className="char-item">
                    <div className="char-value">{char}</div>
                    <div>Count: {freq}</div>
                  </div>
                ))}
              </div>
            ) : (
              <div style={{ height: 300, width: '100%' }}>
                <ResponsiveBar
                  data={freqArray}
                  keys={[ 'count' ]}
                  indexBy="char"
                  margin={{ top: 20, right: 20, bottom: 40, left: 50 }}
                  padding={0.3}
                  valueScale={{ type: 'linear' }}
                  indexScale={{ type: 'band', round: true }}
                  axisBottom={{
                    tickSize: 5,
                    tickPadding: 5,
                    tickRotation: 0,
                    legend: 'Character',
                    legendPosition: 'middle',
                    legendOffset: 32
                  }}
                  axisLeft={{
                    tickValues: Array.from({ length: Math.max(...freqArray.map(d => d.count)) + 1 }, (_, i) => i),
                    format: v => v,
                    legend: 'Frequency',
                    legendPosition: 'middle',
                    legendOffset: -40
                  }}
                  gridYValues={Array.from({ length: Math.max(...freqArray.map(d => d.count)) + 1 }, (_, i) => i)}
                  colors={{ scheme: 'nivo' }}
                />
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  )
}
