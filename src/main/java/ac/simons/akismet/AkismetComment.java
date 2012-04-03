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

import static org.apache.commons.lang.StringUtils.isBlank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

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

	private boolean areRequiredFieldsFilled() {
		return !(isBlank(this.getUserIp()) || this.getUserAgent() == null);
	}
	
	/**
	 * Converts this comment to a HttpEntity to use in a akismet call
	 * @return
	 * @throws Exception
	 */
	public UrlEncodedFormEntity toEntity(final String apiConsumer) throws Exception {
		final List<NameValuePair> p = new ArrayList<NameValuePair>();
		
		if(!areRequiredFieldsFilled())
			throw new AkismetException("The fields blog, userIp and userAgent are required!");
		
		p.add(new BasicNameValuePair("blog", apiConsumer));
		p.add(new BasicNameValuePair("user_ip", this.getUserIp()));
		p.add(new BasicNameValuePair("userAgent", this.getUserAgent()));
		
		if(!isBlank(this.getReferrer()))
			p.add(new BasicNameValuePair("referrer", this.getReferrer()));
		if(!isBlank(this.getPermalink()))
			p.add(new BasicNameValuePair("permalink", this.getPermalink()));
		if(!isBlank(this.getCommentType()))
			p.add(new BasicNameValuePair("comment_type", this.getCommentType()));
		if(!isBlank(this.getCommentAuthor()))
			p.add(new BasicNameValuePair("comment_author", this.getCommentAuthor()));
		if(!isBlank(this.getCommentAuthorEmail()))
			p.add(new BasicNameValuePair("comment_author_email", this.getCommentAuthorEmail()));
		if(!isBlank(this.getCommentAuthorUrl()))
			p.add(new BasicNameValuePair("comment_author_url", this.getCommentAuthorUrl()));
		if(!isBlank(this.getCommentContent()))
			p.add(new BasicNameValuePair("comment_content", this.getCommentContent()));
		
		return new UrlEncodedFormEntity(p, "UTF-8");
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

	@Override
	public String toString() {
		return "AkismetComment [userIp=" + userIp + ", userAgent=" + userAgent
				+ ", referrer=" + referrer + ", permalink=" + permalink
				+ ", commentType=" + commentType + ", commentAuthor="
				+ commentAuthor + ", commentAuthorEmail=" + commentAuthorEmail
				+ ", commentAuthorUrl=" + commentAuthorUrl
				+ ", commentContent=" + commentContent + "]";
	}
}