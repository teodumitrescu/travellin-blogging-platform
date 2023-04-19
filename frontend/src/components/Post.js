import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

function Post() {
  const { id } = useParams();
  const [blogPost, setBlogPost] = useState(null);

  useEffect(() => {
    async function fetchBlogPost() {
      try {
        const response = await axios.get(`http://localhost:8080/blogposts/${id}`);
        setBlogPost(response.data);
      } catch (error) {
        console.error(error);
      }
    }

    fetchBlogPost();
  }, [id]);

  if (!blogPost) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h2>{blogPost.title}</h2>
      <img src={blogPost.images[0]?.url || "https://via.placeholder.com/200"} style={{ width: '200px', height: '200px' }} />
      <p><strong>Updated at:</strong> {blogPost.updatedAt}</p>
      <p><strong>Destination:</strong> {blogPost.destination.name}, {blogPost.destination.country}</p>
      <p><strong>Body:</strong></p>
      <div dangerouslySetInnerHTML={{ __html: blogPost.body }} />
    </div>
  );
}

export default Post
