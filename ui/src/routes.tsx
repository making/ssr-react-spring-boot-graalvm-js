import Posts, {PostsProps} from "./Posts.jsx";
import Post, {PostProps} from "./Post.jsx";
import {RouteObject} from "react-router-dom";

export default function routes(initData: object): RouteObject[] {
    return [
        {
            path: "/", element: <Posts {...initData as PostsProps}/>
        },
        {
            path: "/posts", element: <Posts {...initData as PostsProps}/>
        },
        {
            path: "/posts/:id", element: <Post {...initData as PostProps}/>
        }
    ];
}