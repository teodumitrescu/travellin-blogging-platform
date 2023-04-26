import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../tables.css'
import Navbar from './Navbar';
import ReactModal from 'react-modal';

function Destinations() {
  const [destinations, setDestinations] = useState([]);
  const [isLoggedIn, setIsLoggedIn] = useState(true);
  const [name, setName] = useState('');
  const [country, setCountry] = useState('');
  const [id, setId] = useState('');
  const [isAdding, setIsAdding] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [searchField, setSearchField] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [modalIsOpen, setModalIsOpen] = useState(false);

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

  const handleModal = () => {
    setModalIsOpen(!modalIsOpen);
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
        handleModal();
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
        handleModal();
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
        handleModal();
      })
      .catch(error => console.error(error));
      
    })
      .catch(error => console.error(error));
  };

  const handleSearch = async () => {
    setIsLoading(true);
    try {
      let response;
	  if (searchField === '') {
        response = await axios.get('http://localhost:8080/destinations', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
	  } else {
        response = await axios.get(`http://localhost:8080/destinations/search/${searchField}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
      }

    if (response && response.data) {
		    setDestinations(response.data);
	  }
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
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

      <div>
        <ReactModal isOpen={modalIsOpen} style={{
            content: {
              width: '30%',
              height: '30%',
              margin: 'auto',
              top: 0,
              bottom: 0,
              left: 0,
              right: 0,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              backgroundColor: '#f8f8f8',
              flexDirection: 'column'
            }
          }}
        >
          <h2>Action successful!</h2>
          <button onClick={handleModal}>Dismiss</button>
        </ReactModal>
      </div>

      <h2>Destinations</h2>
      <div>
		    <label htmlFor="searchField">Search by Country:</label>
		    <input type="text" value={searchField} onChange={(e) => setSearchField(e.target.value)} />
		    <button onClick={handleSearch}>Search</button>
	    </div>
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
