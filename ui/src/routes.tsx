import { RouteObject } from "react-router-dom";
import PostsPage, { PostsPageProps } from "./pages/PostsPage";
import PostDetailPage, { PostDetailPageProps } from "./pages/PostDetailPage";

export default function routes(initData: object): RouteObject[] {
  return [
    {
      path: "/", 
      element: <PostsPage {...initData as PostsPageProps}/>
    },
    {
      path: "/posts", 
      element: <PostsPage {...initData as PostsPageProps}/>
    },
    {
      path: "/posts/:id", 
      element: <PostDetailPage {...initData as PostDetailPageProps}/>
    }
  ];
}