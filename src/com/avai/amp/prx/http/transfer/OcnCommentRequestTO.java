package com.avai.amp.prx.http.transfer;

import java.util.Date;

import com.avai.amp.prx.lib.LibraryApplication;

public class OcnCommentRequestTO {
	private String commentId;
	private String postId;
	private String userId;
	private String datePosted;
	private String content;

	public OcnCommentRequestTO(String commentId, String postId, String userId, Date datePosted, String content) {
		this.commentId = commentId;
		this.postId = postId;
		this.userId = userId;
		this.datePosted = LibraryApplication.toISOString(datePosted);
		this.content = content;
	}

	public String getCommentId() {
		return commentId;
	}

	public String getPostId() {
		return postId;
	}

	public String getUserId() {
		return userId;
	}

	public Date getDatePosted() {
		return LibraryApplication.toISODate(datePosted);
	}

	public String getContent() {
		return content;
	}
}
