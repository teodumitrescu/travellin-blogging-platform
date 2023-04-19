import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../tables.css'
import Navbar from './Navbar';

function Destinations() {
  const [destinations, setDestinations] = useState([]);
  const token = localStorage.getItem('token');
  const [isLoggedIn, setIsLoggedIn] = useState(true); // Change this to false if you want to test the navbar as if the user is not logged in

  const handleLogout = () => {
    setIsLoggedIn(false);
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
