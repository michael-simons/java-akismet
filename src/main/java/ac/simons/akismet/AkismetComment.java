/**
 * Created by Michael Simons, michael-simons.eu
 * and released under The BSD License
 * http://www.opensource.org/licenses/bsd-license.php
 *
 * Copyright (c) 2011, Michael Simons
 * All rights reserved.
 *
 * Redistribution  and  use  in  source   and  binary  forms,  with  or   without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source   code must retain   the above copyright   notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary  form must reproduce  the above copyright  notice,
 *   this list of conditions  and the following  disclaimer in the  documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name  of  michael-simons.eu   nor the names  of its contributors
 *   may be used  to endorse   or promote  products derived  from  this  software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS  PROVIDED BY THE  COPYRIGHT HOLDERS AND  CONTRIBUTORS "AS IS"
 * AND ANY  EXPRESS OR  IMPLIED WARRANTIES,  INCLUDING, BUT  NOT LIMITED  TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL  THE COPYRIGHT HOLDER OR CONTRIBUTORS  BE LIABLE
 * FOR ANY  DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR  CONSEQUENTIAL
 * DAMAGES (INCLUDING,  BUT NOT  LIMITED TO,  PROCUREMENT OF  SUBSTITUTE GOODS OR
 * SERVICES; LOSS  OF USE,  DATA, OR  PROFITS; OR  BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT  LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE  USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ac.simons.akismet;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**
 * Represents a comment send to akismet for spam check. From the api:<br>
 * This is basically the core of everything. This call takes a number of arguments 
 * and characteristics about the submitted content and then returns a thumbs up or 
 * thumbs down. 
 * Almost everything is optional, but performance can drop dramatically if you 
 * exclude certain elements. I would recommend erring on the side of too much data, 
 * as everything is used as part of the Akismet signature. 
 * @author Michael J. Simons
 *
 */
public class AkismetComment implements Serializable {	
	private static final long serialVersionUID = -5782832268604549364L;

	/** 
	 * The front page or home URL of the instance making the request. For a blog
	 * or wiki this would be the front page. Note: Must be a full URI, 
	 * including http://. (required)
	 */
	private String blog;
	
	/** IP address of the comment submitter. (required) */
	private String userIp;
	
	/** 
	 * User agent string of the web browser submitting the comment - typically 
	 * the HTTP_USER_AGENT cgi variable. Not to be confused with the user agent 
	 * of your Akismet library. (required)
	 */
	private String userAgent;
	
	/** The content of the HTTP_REFERER header should be sent here. */
	private String referrer;
	
	/** The permanent location of the entry the comment was submitted to. */
	private String permalink;
	
	/** May be blank, comment, trackback, pingback, or a made up value like "registration". */
	private String commentType;
	
	/** Name submitted with the comment */
	private String commentAuthor;
	
	/** Email address submitted with the comment */
	private String commentAuthorEmail;
	
	/** URL submitted with comment */
	private String commentAuthorUrl;
	
	/** The content that was submitted. */
	private String commentContent;

	@XmlElement(name="blog")
	public String getBlog() {
		return blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	@XmlElement(name="user_ip")
	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@XmlElement(name="user_agent")
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@XmlElement(name="referrer")
	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	@XmlElement(name="permalink")
	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	@XmlElement(name="comment_type")
	public String getCommentType() {
		return commentType;
	}

	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	@XmlElement(name="comment_author")
	public String getCommentAuthor() {
		return commentAuthor;
	}

	public void setCommentAuthor(String commentAuthor) {
		this.commentAuthor = commentAuthor;
	}

	@XmlElement(name="comment_author_email")
	public String getCommentAuthorEmail() {
		return commentAuthorEmail;
	}

	public void setCommentAuthorEmail(String commentAuthorEmail) {
		this.commentAuthorEmail = commentAuthorEmail;
	}

	@XmlElement(name="comment_author_url")
	public String getCommentAuthorUrl() {
		return commentAuthorUrl;
	}

	public void setCommentAuthorUrl(String commentAuthorUrl) {
		this.commentAuthorUrl = commentAuthorUrl;
	}

	@XmlElement(name="comment_content")
	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}	
}