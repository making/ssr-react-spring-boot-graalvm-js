import React from 'react'
import {Post as PostModel} from "./types.ts";
import {Link} from "react-router-dom";
import useSWR, {Fetcher} from 'swr'

export interface PostsProps {
    preLoadedPosts: PostModel[];
}

const Posts: React.FC<PostsProps> = ({preLoadedPosts}) => {
    const isPreLoaded = !!preLoadedPosts;
    const fetcher: Fetcher<PostModel[], string> = (url) => fetch(url).then(res => res.json());
    const {data, isLoading} = useSWR(isPreLoaded ? null : '/api/posts', fetcher);
    const posts = data || preLoadedPosts;
    if (isLoading || !posts) {
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
