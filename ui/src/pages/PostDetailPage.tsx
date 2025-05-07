import React from 'react';
import {Link, useParams} from 'react-router-dom';
import {ArrowLeft, Calendar} from 'lucide-react';
import useSWR, {Fetcher} from 'swr';
import {Post as PostModel} from '../types';
import MainLayout from '../layouts/MainLayout';
import Loader from '../components/Loader';

export interface PostDetailPageProps {
    preLoadedPost: PostModel;
}

const PostDetailPage: React.FC<PostDetailPageProps> = ({preLoadedPost}) => {
    const {id} = useParams();
    const isPreLoaded = preLoadedPost && preLoadedPost.id === Number(id);
    const fetcher: Fetcher<PostModel, string> = (id) => fetch(`/api/posts/${id}`).then(
        res => res.json());
    const {data, isLoading} = useSWR(isPreLoaded ? null : id, fetcher);
    const post = data || preLoadedPost;

    if (isLoading || !post) {
        return (
            <MainLayout>
                <Loader/>
            </MainLayout>
        );
    }

    // For demonstration purposes, generate a fake date based on post ID
    const postDate = new Date(Date.now() - (post.id * 86400000)).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    });

    // Generate paragraphs from the post body
    const paragraphs = post.body.split('\n\n').length > 1
        ? post.body.split('\n\n')
        : post.body.split('. ').map(sentence => sentence.trim());

    return (
        <MainLayout>
            <div className="mb-6">
                <Link
                    to="/"
                    className="inline-flex items-center text-primary-600 hover:text-primary-700 transition-colors font-medium"
                >
                    <ArrowLeft size={18} className="mr-1"/>
                    Back to posts
                </Link>
            </div>

            <article className="bg-bg-primary rounded-lg shadow-xs p-6 md:p-8">
                <header className="mb-6 border-b border-border-primary pb-4">
                    <h1 className="text-3xl font-bold text-text-primary mb-3">{post.title}</h1>
                    <div className="flex items-center text-text-muted text-sm">
                        <Calendar size={16} className="mr-1"/>
                        <time>{postDate}</time>
                    </div>
                </header>

                <div className="space-y-4">
                    {paragraphs.map((paragraph, index) => (
                        <p key={index} className="leading-relaxed text-text-secondary">
                            {paragraph}
                        </p>
                    ))}
                </div>
            </article>
        </MainLayout>
    );
};

export default PostDetailPage;
