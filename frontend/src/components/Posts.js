import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../tables.css'
import Navbar from './Navbar';

function Posts() {
  const [posts, setPosts] = useState([]);
  const [searchField, setSearchField] = useState('');
  const [searchCriteria, setSearchCriteria] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const token = localStorage.getItem('token');
  const [isLoggedIn, setIsLoggedIn] = useState(true);
  const [searchCount, setSearchCount] = useState(0);


  const handleLogout = () => {
    setIsLoggedIn(false);
  };

  const handleSearch = async () => {
    setIsLoading(true);
    try {
      let response;
	  if (searchField === '') {
        response = await axios.get('http://localhost:8080/blogposts', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
	  } else if (searchCriteria === 'author') {
        response = await axios.get(`http://localhost:8080/blogposts/search/author/${searchField}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
      } else if (searchCriteria === 'title') {
        response = await axios.get(`http://localhost:8080/blogposts/search/title/${searchField}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
	} else if (searchCriteria === 'destination') {
        response = await axios.get(`http://localhost:8080/blogposts/search/destination/${searchField}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
      }

      if (response && response.data) {
		setPosts(response.data);
		setSearchCount(count => count + 1);
	  }
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
	<div>
	  {isLoggedIn && (
		<Navbar isLoggedIn={isLoggedIn} handleLogout={handleLogout} />
	  )}
	  <h2>Blog Posts</h2>
	  <div>
		<label htmlFor="searchField">Search by:</label>
		<select id="searchCriteria" value={searchCriteria} onChange={(e) => setSearchCriteria(e.target.value)}>
		  <option value="title">Title</option>
		  <option value="author">Author</option>
		  <option value="destination">Destination</option>
		</select>
		<input type="text" value={searchField} onChange={(e) => setSearchField(e.target.value)} />
		<button onClick={handleSearch}>Search</button>
	  </div>
	  <table>
		<thead>
		  <tr>
			<th>Title</th>
			<th>Content</th>
			<th>Author</th>
			<th>Created At</th>
			<th>Updated At</th>
			<th>Destination</th>
			<th>Images</th>
			<th>Tags</th>
		  </tr>
		</thead>
		<tbody>
		  {posts.length > 0 ? (
			posts.map((post) => (
			  <tr key={post.id}>
				<td>{post.title}</td>
				<td>{post.content}</td>
				<td>{post.author.username}</td>
				<td>{new Date(post.createdAt).toLocaleString()}</td>
				<td>{new Date(post.updatedAt).toLocaleString()}</td>
				<td>{post.destination ? post.destination.name : ''}</td>
				<td>{post.images.map(image => <img src={image.url} alt={image.altText} key={image.id} />)}</td>
				<td>{post.tags.map(tag => tag.name).join(', ')}</td>
			  </tr>
			))
		  ) : (
			<tr>
			  <td colSpan="8">No results</td>
			</tr>
		  )}
		</tbody>
	  </table>
	</div>
  );
}

export default Posts;
