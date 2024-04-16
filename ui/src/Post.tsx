import React, {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {Post as PostModel} from "./types.ts";

export interface PostProps {
    preLoadedPost: PostModel;
}

const Post: React.FC<PostProps> = ({preLoadedPost}) => {
    const [post, setPost] = useState<PostModel>(preLoadedPost || {});
    const [loading, setLoading] = useState(true);
    const {id} = useParams();

    useEffect(() => {
        if (id && (!preLoadedPost || preLoadedPost.id !== Number(id))) {
            fetch(`/api/posts/${id}`)
                .then(res => res.json())
                .then(data => setPost(data))
                .finally(() => setLoading(false));
        } else {
            setLoading(false);
        }
    }, [preLoadedPost, id]);

    if (loading) {
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