import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../tables.css'
import Navbar from './Navbar';

function Posts() {
  const [posts, setPosts] = useState([]);
  const token = localStorage.getItem('token');
  const [isLoggedIn, setIsLoggedIn] = useState(true); // Change this to false if you want to test the navbar as if the user is not logged in

  const handleLogout = () => {
    setIsLoggedIn(false);
  };

  useEffect(() => {
    axios.get('http://localhost:8080/blogposts', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
      .then(response => {
        setPosts(response.data);
      })
      .catch(error => console.error(error));
  }, [token]);

  return (
    <div>
		{isLoggedIn && (
        <Navbar isLoggedIn={isLoggedIn} handleLogout={handleLogout} />
      )}
      <h2>Blog Posts</h2>
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
          {posts.map(post => (
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
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Posts;
