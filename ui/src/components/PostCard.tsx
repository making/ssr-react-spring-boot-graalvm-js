import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import { Post as PostModel } from '../types';

interface PostCardProps {
  post: PostModel;
}

const PostCard: React.FC<PostCardProps> = ({ post }) => {
  // Create a preview of the body text
  const previewText = post.body.length > 120 
    ? `${post.body.substring(0, 120)}...` 
    : post.body;
  
  return (
    <article className="bg-bg-primary rounded-lg shadow-xs overflow-hidden hover:shadow-md transition-shadow p-6">
      <Link to={`/posts/${post.id}`} className="block">
        <h2 className="text-xl font-bold text-text-primary mb-2 hover:text-primary-600 transition-colors">
          {post.title}
        </h2>
        <div className="text-text-muted text-sm mb-3">
          {/* For demonstration, we're using the post ID to create a fake date */}
          <time>
            {new Date(Date.now() - (post.id * 86400000)).toLocaleDateString('en-US', {
              year: 'numeric',
              month: 'long',
              day: 'numeric'
            })}
          </time>
        </div>
        <p className="text-text-secondary mb-4">{previewText}</p>
        <div className="flex items-center text-primary-600 font-medium">
          <span>Read more</span>
          <ArrowRight size={16} className="ml-1" />
        </div>
      </Link>
    </article>
  );
};

export default PostCard;
