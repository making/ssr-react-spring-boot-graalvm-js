import React from 'react';
import useSWR, { Fetcher } from 'swr';
import { Post as PostModel } from '../types';
import PostCard from '../components/PostCard';
import MainLayout from '../layouts/MainLayout';
import Loader from '../components/Loader';

export interface PostsPageProps {
  preLoadedPosts: PostModel[];
}

const PostsPage: React.FC<PostsPageProps> = ({ preLoadedPosts }) => {
  const isPreLoaded = !!preLoadedPosts;
  const fetcher: Fetcher<PostModel[], string> = (url) => fetch(url).then(res => res.json());
  const { data, isLoading } = useSWR(isPreLoaded ? null : '/api/posts', fetcher);
  const posts = data || preLoadedPosts;

  if (isLoading || !posts) {
    return (
      <MainLayout>
        <Loader />
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-text-primary mb-2">Latest Posts</h1>
        <p className="text-text-secondary">Discover interesting articles from our blog</p>
      </div>
      
      <div className="grid gap-6 md:grid-cols-2">
        {posts.map(post => (
          <PostCard key={post.id} post={post} />
        ))}
      </div>
    </MainLayout>
  );
};

export default PostsPage;
