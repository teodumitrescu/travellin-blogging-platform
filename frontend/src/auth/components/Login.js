import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import '../../forms.css'
import '../../App.css'
import axios from 'axios';

function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const login = (e) => {
    e.preventDefault()

    axios.post('/api/login', { username, password })
      .then((response) => {
        const data = response.data;
        if (data.token) {
          localStorage.setItem('token', data.token)
          navigate.push('/profile')
        } else {
          setError(data.message)
        }
      })
      .catch((err) => {
        setError(err.message)
      })
  }

  return (
    <div className="home-container" style={{ height: '100vh', overflow: 'hidden' }}>
      <div className='center'>
        <div className='auth'>
          <h1>Log in</h1>
          {error && <div className='auth__error'>{error}</div>}
          <form onSubmit={login} name='login_form'>
            <div className="auth-form-container">
              <input
                type='username'
                value={username}
                required
                placeholder="Enter your username"
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>

            <div className="auth-form-container">
              <input
                type='password'
                value={password}
                required
                placeholder='Enter your password'
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>

            <button className="secondary-button" type='submit'>
              Login
            </button>
          </form>
          <p>
            Don't have an account?
            <Link to='/register'>Create one here</Link>
          </p>
        </div>
      </div>
    </div>
  )
}

export default Login
