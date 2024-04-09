import React, {useEffect, useState} from 'react'
import Post from "./Post.jsx";
import {Post as PostModel} from "./types.ts";

export interface PostsProps {
    preLoadedPosts: PostModel[];
}

const Posts: React.FC<PostsProps> = ({preLoadedPosts}) => {
    const [posts, setPosts] = useState<PostModel[]>(preLoadedPosts || []);

    useEffect(() => {
        if (!preLoadedPosts) {
            fetch('/api/posts')
                .then(res => res.json())
                .then(data => setPosts(data));
        }
    }, [preLoadedPosts]);

    return (<>
        <div>
            <h2>Posts</h2>
            {posts.map(post => <Post preLoadedPost={post} key={post.id} showDetails={false}/>)}
        </div>
    </>)
}

export default Posts
