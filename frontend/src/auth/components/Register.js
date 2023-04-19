import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../../forms.css';
import "../../App.css";
import jwt_decode from 'jwt-decode';

function Register() {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [bio, setBio] = useState('');
  const [profileImageUrl, setProfileImageUrl] = useState('');
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
        const userResponse = await axios.post('http://localhost:8080/api/auth/register', {
          username,
          email,
          password,
        });
        if (userResponse.data.token) {
          localStorage.setItem('token', userResponse.data.token);
          const decodedToken = jwt_decode(userResponse.data.token)
          localStorage.setItem('userId', decodedToken.userId)
          
          const profileResponse = await axios.post('http://localhost:8080/profiles/create', {
            firstName,
            lastName,
            bio,
            profileImageUrl,
          }, {
            headers: {
              Authorization: `Bearer ${userResponse.data.token}`,
            },
          });
          if (profileResponse.data.id) {
            navigate('/home');
          }
        }
      } catch (err) {
        setError(err.response.data.message);
      }
    }
    setUsername('');
    setEmail('');
    setPassword('');
    setConfirmPassword('');
    setBio('');
    setProfileImageUrl('');
  };


  return (
    <div className="login-container" style={{height: '100vh', overflow: 'hidden'}}>
      <div className='login-card'>
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
            <div className="auth-form-container">
              <input
                type='bio'
                value={bio}
                required
                placeholder='Write something about yourself.'
                onChange={(e) => setBio(e.target.value)}
              />
            </div>
            <div className="auth-form-container">
              <input
                type='profile URL'
                value={profileImageUrl}
                required
                placeholder='Add a link with your picture.'
                onChange={(e) => setProfileImageUrl(e.target.value)}
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
