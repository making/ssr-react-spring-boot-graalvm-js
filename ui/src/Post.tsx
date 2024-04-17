import React from "react";
import {Link, useParams} from "react-router-dom";
import {Post as PostModel} from "./types.ts";
import useSWR, {Fetcher} from 'swr'

export interface PostProps {
    preLoadedPost: PostModel;
}

const Post: React.FC<PostProps> = ({preLoadedPost}) => {
    const {id} = useParams();
    const isPreLoaded = preLoadedPost && preLoadedPost.id == Number(id);
    const fetcher: Fetcher<PostModel, string> = (id) => fetch(`/api/posts/${id}`).then(res => res.json());
    const {data, isLoading} = useSWR(isPreLoaded ? null : id, fetcher);
    const post = data || preLoadedPost;
    if (isLoading || !post) {
        return <div>Loading ...</div>
    }
    return <>
        <h3><Link to={`/posts/${post.id}`}>{post.title}</Link></h3>
        <p>{post.body}</p>
        <hr/>
        <Link to={'/'}>&laquo; Go to Posts</Link>
    </>;
};

export default Post;