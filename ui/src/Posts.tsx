import React, {useEffect, useState} from 'react'
import {Post as PostModel} from "./types.ts";
import {Link} from "react-router-dom";

export interface PostsProps {
    preLoadedPosts: PostModel[];
}

const Posts: React.FC<PostsProps> = ({preLoadedPosts}) => {
    const [posts, setPosts] = useState<PostModel[]>(preLoadedPosts || []);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!preLoadedPosts) {
            fetch('/api/posts')
                .then(res => res.json())
                .then(data => setPosts(data))
                .finally(() => setLoading(false));
        } else {
            setLoading(false);
        }
    }, [preLoadedPosts]);

    if (loading) {
        return <div>Loading ...</div>
    }
    return (<>
        <div>
            <h2>Posts</h2>
            <ul>
                {posts.map(post => <li key={post.id}><Link to={`/posts/${post.id}`}>{post.title}</Link></li>)}
            </ul>
        </div>
    </>)
}

export default Posts
