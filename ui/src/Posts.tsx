import React, {useEffect, useState} from 'react'
import Post from "./Post.jsx";

export interface PostsProps {
    preLoadedPosts: Post[];
}

const Posts: React.FC<PostsProps> = ({preLoadedPosts}) => {
    const [posts, setPosts] = useState(preLoadedPosts || []);

    useEffect(() => {
        if (!preLoadedPosts) {
            fetch('/api/posts')
                .then(res => res.json())
                .then(data => setPosts(data));
        }
    }, []);

    return (<>
        <div>
            <h2>Posts</h2>
            {posts.map(post => <Post preLoadedPost={post} key={post.id} showDetails={false}/>)}
        </div>
    </>)
}

export default Posts
