import { useState, useEffect } from 'react'
import axios from 'axios'
import Navbar from './Navbar'

function Profile() {
  const [profile, setProfile] = useState(null)
  const [error, setError] = useState('')
  const [isLoggedIn, setIsLoggedIn] = useState(true); // Change this to false if you want to test the navbar as if the user is not logged in
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    bio: '',
    profileImageUrl: ''
  });

  const handleLogout = () => {
    setIsLoggedIn(false);
  };

  useEffect(() => {
    const userId = localStorage.getItem('userId')
    const token = localStorage.getItem('token')
    axios.get(`http://localhost:8080/profiles/user=${userId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(response => {
        setProfile(response.data)
        setFormData(response.data)
      })
      .catch(error => {
        setError(error.message)
      })
  }, [])

  const handleSubmit = (event) => {
    event.preventDefault();
    const userId = localStorage.getItem('userId')
    const token = localStorage.getItem('token')
    axios.put(`http://localhost:8080/profiles/${profile.id}`, formData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then(response => {
        setProfile(response.data)
        setFormData(response.data)
        setIsEditing(false)
      })
      .catch(error => {
        setError(error.message)
      })
  }

  const handleChange = (event) => {
    setFormData({ ...formData, [event.target.name]: event.target.value })
  }

  if (error) {
    return <div>Error: {error}</div>
  }

  if (!profile) {
    return <div>Loading...</div>
  }

  return (
    <div>
      {isLoggedIn && (
        <Navbar isLoggedIn={isLoggedIn} handleLogout={handleLogout} />
      )}
      {isEditing ? (
        <form onSubmit={handleSubmit}>
          <label>
            First Name:
            <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} />
          </label>
          <br />
          <label>
            Last Name:
            <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} />
          </label>
          <br />
          <label>
            Bio:
            <input type="text" name="bio" value={formData.bio} onChange={handleChange} />
          </label>
          <br />
          <label>
            Profile Image URL:
            <input type="text" name="profileImageUrl" value={formData.profileImageUrl} onChange={handleChange} />
          </label>
          <br />
          <button type="submit">Update Profile</button>
        </form>
      ) : (
        <div>
          <h1>{profile.firstName} {profile.lastName}</h1>
          <p>Bio: {profile.bio}</p>
          <p>Profile picture:</p>
          <img src={profile.profileImageUrl} />
          <button onClick={() => setIsEditing(true)}>Edit Profile</button>
        </div>
      )}
    </div>
  )
}

export default Profile
