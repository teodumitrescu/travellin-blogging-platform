import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../forms.css';
import "../../App.css";

function Register() {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const validatePassword = () => {
    if (password !== '' && confirmPassword !== '') {
      if (password !== confirmPassword) {
        setError('Passwords do not match');
        return false;
      }
    }
    return true;
  };

  const register = async (e) => {
    e.preventDefault();
    setError('');
    if (validatePassword()) {
      try {
        const response = await axios.post('/api/register', {
          email,
          password,
        });
        if (response.data.token) {
          localStorage.setItem('token', response.data.token);
          navigate.push('/verify-email');
        }
      } catch (err) {
        setError(err.response.data.message);
      }
    }
    setEmail('');
    setPassword('');
    setConfirmPassword('');
  };

  return (
    <div className="home-container" style={{height: '100vh', overflow: 'hidden'}}>
      <div className='center'>
        <div className='auth'>
          <h1>Register</h1>
          {error && <div className='auth__error'>{error}</div>}
          <form onSubmit={register} name='registration_form'>
			<div className="auth-form-container">
			<input
				type='text'
				value={firstName}
				placeholder="Enter your first name"
				required
				onChange={(e) => setFirstName(e.target.value)}
			/>
			</div>
			<div className="auth-form-container">
			<input
				type='text'
				value={lastName}
				placeholder="Enter your last name"
				required
				onChange={(e) => setLastName(e.target.value)}
			/>
			</div>
			<div className="auth-form-container">
			<input
				type='text'
				value={username}
				placeholder="Enter your username"
				required
				onChange={(e) => setUsername(e.target.value)}
			/>
			</div>
            <div className="auth-form-container">
              <input
                type='email'
                value={email}
                placeholder="Enter your email"
                required
                onChange={(e) => setEmail(e.target.value)}
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
            <div className="auth-form-container">
              <input
                type='password'
                value={confirmPassword}
                required
                placeholder='Confirm password'
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
            </div>
            <button className="secondary-button" type='submit'>Register</button>
          </form>
          <span>
            Already have an account?
            <Link to='/login'>Login</Link>
          </span>
        </div>
      </div>
    </div>
  );
}

export default Register;
