import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../tables.css'
import Navbar from './Navbar';

function Destinations() {
  const [destinations, setDestinations] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(true);
  const [name, setName] = useState('');
  const [country, setCountry] = useState('');
  const [id, setId] = useState('');
  const [isAdding, setIsAdding] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [isEditing, setIsEditing] = useState(false);

  const token = localStorage.getItem('token');

  const handleLogout = () => {
    setIsLoggedIn(false);
  };

  const handleAddDestination = () => {
    setIsAdding(!isAdding);
  };

  const handleDeleteDestination = () => {
    setIsDeleting(!isDeleting);
  };

  const handleEditDestination = () => {
    setIsEditing(!isEditing);
  };

  const handleSaveDestination = () => {
    const data = { name, country };
    axios.post('http://localhost:8080/destinations', data, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
      .then(response => {
        setDestinations([...destinations, response.data]);
        setName('');
        setCountry('');
        setIsAdding(false);
      })
      .catch(error => console.error(error));
  };

  const handleSaveEdit = () => {
    const data = { name, country};
    axios.put(`http://localhost:8080/destinations/${id}`, data, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then(() => {
      setId('');
      setName('');
      setCountry('');
      setIsEditing(false);
      axios.get('http://localhost:8080/destinations', {
      headers: {
        Authorization: `Bearer ${token}`
      }
      })
      .then(response => {
        setDestinations(response.data);
      })
      .catch(error => console.error(error));
      
    })
      .catch(error => console.error(error));
  };

  const handleConfirmDelete = () => {
    axios.delete(`http://localhost:8080/destinations/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then(() => {
      setId('');
      setName('');
      setCountry('');
      setIsDeleting(false);
      axios.get('http://localhost:8080/destinations', {
      headers: {
        Authorization: `Bearer ${token}`
      }
      })
      .then(response => {
        setDestinations(response.data);
      })
      .catch(error => console.error(error));
      
    })
      .catch(error => console.error(error));
  };

  useEffect(() => {
    axios.get('http://localhost:8080/destinations', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
      .then(response => {
        setDestinations(response.data);
      })
      .catch(error => console.error(error));
  }, [token]);

  return (
    <div>
		{isLoggedIn && (
        <Navbar isLoggedIn={isLoggedIn} handleLogout={handleLogout} />
      )}
      <h2>Destinations</h2>
      <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
      {!isAdding ? (
          <button onClick={handleAddDestination}>Add</button>
        ) : (
          <div>
            <input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Name" />
            <input type="text" value={country} onChange={(e) => setCountry(e.target.value)} placeholder="Country" />
            <button onClick={handleSaveDestination}>Save</button>
            <button onClick={handleAddDestination}>Cancel</button>
          </div> )}

      {!isEditing ? (
          <button onClick={handleEditDestination}>Edit</button>
        ) : (
          <div>
            <input type="text" value={id} onChange={(e) => setId(e.target.value)} placeholder="Id" />
            <input type="text" value={name} onChange={(e) => setName(e.target.value)} placeholder="Name" />
            <input type="text" value={country} onChange={(e) => setCountry(e.target.value)} placeholder="Country" />
            <button onClick={handleSaveEdit}>Save</button>
            <button onClick={handleEditDestination}>Cancel</button>
          </div> )}


      {!isDeleting ? (
        <button onClick={handleDeleteDestination}>Delete</button>
      ) : (
        <div>
          <input type="text" value={id} onChange={(e) => setId(e.target.value)} placeholder="Id" />
          <button onClick={handleConfirmDelete}>Save</button>
          <button onClick={handleDeleteDestination}>Cancel</button>
        </div> )}
      </div>
      <table>
        <thead>
          <tr>
		        <th>Id</th>
            <th>Name</th>
            <th>Country</th>
			      <th>Number of posts</th>
          </tr>
        </thead>
        <tbody>
          {destinations.map(destination => (
            <tr key={destination.id}>
              <td>{destination.id}</td>
              <td>{destination.name}</td>
              <td>{destination.country}</td>
			        <td>{destination.numberOfPosts}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Destinations;
