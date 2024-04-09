import React, {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {Post as PostModel} from "./types.ts";

export interface PostProps {
    preLoadedPost: PostModel;
    showDetails: boolean;
}

const Post: React.FC<PostProps> = ({preLoadedPost, showDetails}) => {
    const [post, setPost] = useState<PostModel>( {
        id: 0,
        title: 'Loading ...',
        body: ''
    });
    const {id} = useParams();

    useEffect(() => {
        if (id && (!preLoadedPost || preLoadedPost.id !== Number(id))) {
            fetch(`/api/posts/${id}`)
                .then(res => res.json())
                .then(data => setPost(data));
        } else if (preLoadedPost) {
            setPost(preLoadedPost);
        }
    }, [preLoadedPost, id]);

    return <>
        <h3><Link to={`/posts/${post.id}`}>{post.title}</Link></h3>
        {showDetails && <>
            <p>{post.body}</p>
            <hr/>
            <Link to={'/'}>&laquo; Go to Posts</Link>
        </>}
    </>;
};

export default Post;