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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loosely oriented at http://akismet.com/development/api/
 * @author Michael J. Simons
 */
public class Akismet {
	/** The default user agent */
	private final String userAgent;
	/** The default content type */
	private final String contentType = "application/x-www-form-urlencoded; charset=utf-8";
	/** A logger */
	private final Logger logger = LoggerFactory.getLogger(Akismet.class);
	
	private final HttpClient httpClient;
	
	private String apiEndpoint = "rest.akismet.com";
	private String apiVersion = "1.1";	
		
	/** The API key being verified for use with the API */
	private String apiKey;
	/**
	 * A.k.a "blog" 
	 * The front page or home URL of the instance making the request. 
	 * For a blog, site, or wiki this would be the front page. Note: Must be a full URI, including http://. 
	 */
	private String apiConsumer;	
	
	/** If set to false, all comments are treated as ham and no akismet calls are made */
	private boolean enabled = true;
	
	public Akismet(HttpClient httpClient) {	
		this.httpClient = httpClient;
		final Properties version = new Properties();
		try {
			version.load(Akismet.class.getResourceAsStream("/ac/simons/akismet/version.properties"));
		} catch(IOException e) {
		}
		this.userAgent = String.format("Java/%s java-akismet/%s", System.getProperty("java.version"), version.getProperty("ac.simons.akismet.version"));
	}

	public String getUserAgent() {
		return userAgent;
	}

	public String getApiEndpoint() {
		return apiEndpoint;
	}

	public void setApiEndpoint(String apiEndpoint) {
		this.apiEndpoint = apiEndpoint;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apikey) {
		this.apiKey = apikey;
	}

	public String getApiConsumer() {
		return apiConsumer;
	}

	public void setApiConsumer(String apiConsumer) {
		this.apiConsumer = apiConsumer;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private HttpPost newHttpPostRequest(final String uri) {
		final HttpPost request = new HttpPost(uri);
		request.setHeader("User-Agent", this.userAgent);
		request.setHeader("Content-Type", this.contentType);
		return request;
	}

	private HttpResponse callAkismet(final String function, final AkismetComment comment) throws Exception {
		final HttpPost request = newHttpPostRequest(String.format("http://%s.%s/%s/%s", this.getApiKey(), this.getApiEndpoint(), this.getApiVersion(), function));
		request.setEntity(comment.toEntity(this.getApiConsumer()));
		return this.getHttpClient().execute(request);
	}

	/**
	 * The key verification call should be made before beginning to use the service. It requires two variables, key and blog.
	 * @return True if the key is valid. This is the one call that can be made without the API key subdomain.
	 * @throws AkismetException
	 */
	public boolean verifyKey() throws AkismetException {
		boolean rv = false;
		try {
			final HttpPost request = newHttpPostRequest(String.format("http://%s/%s/verify-key", this.getApiEndpoint(), this.getApiVersion()));			
			final List<NameValuePair> p = new ArrayList<NameValuePair>();
			p.add(new BasicNameValuePair("key", this.getApiKey()));
			p.add(new BasicNameValuePair("blog", this.getApiConsumer()));			
			request.setEntity(new UrlEncodedFormEntity(p, "UTF-8"));
			final HttpResponse response = this.getHttpClient().execute(request);
			final String body = EntityUtils.toString(response.getEntity());
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				rv = body.trim().equals("valid");
			else 
				logger.warn(String.format("Something bad happened while verifying key, assuming key is invalid: %s", response.getStatusLine().getReasonPhrase()));			
		} catch(Exception e) {
			throw new AkismetException(e);
		}
		return rv;
	}	
	
	/**
	 * This is basically the core of everything. This call takes a number of arguments 
	 * and characteristics about the submitted content and then returns a thumbs up or 
	 * thumbs down.<br>
	 * Almost everything is optional, but performance can drop dramatically if you 
	 * exclude certain elements.<br>
	 * I would recommend erring on the side of too much data, as everything is used as 
	 * part of the Akismet signature.
	 * @return True, if the comment is spam, false otherwise
	 * @throws AkismetException
	 */
	public boolean commentCheck(final AkismetComment comment) throws AkismetException {
		// When in doubt, assume that the comment is ham
		boolean rv = false;
		if(enabled) {
			try {
				final HttpResponse response = this.callAkismet("comment-check", comment);
				final String body = EntityUtils.toString(response.getEntity());
				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
					rv = body.trim().equals("true");
				else 
					logger.warn(String.format("Something bad happened while checking a comment, assuming comment is ham: %s", response.getStatusLine().getReasonPhrase()));
				if(logger.isDebugEnabled())
					logger.debug(String.format("Result for comment %s was: -> %s <-", comment.toString(), (rv ? "spam" : "ham")));
			} catch(Exception e) {
				throw new AkismetException(e);
			}
		}
		return rv;
	}
	
	/**
	 * This call is for submitting comments that weren't marked 
	 * as spam but should have been.
	 * @param comment
	 * @return True if the spam was successfully submitted.
	 * @throws AkismetException
	 */
	public boolean submitSpam(final AkismetComment comment) throws AkismetException {
		boolean rv = false;
		try {
			final HttpResponse response = this.callAkismet("submit-spam", comment);
			final String body = EntityUtils.toString(response.getEntity());
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				logger.warn(String.format("Something bad happened while submitting Spam: %s", response.getStatusLine().getReasonPhrase()));
			else {
				logger.debug(String.format("Spam successfully submitted, response was '%s'", body));
				rv = true;
			}
		} catch(Exception e) {
			throw new AkismetException(e);
		}
		return rv;
	}
	
	/**
	 * This call is intended for the marking of false positives, 
	 * things that were incorrectly marked as spam.
	 * @param comment
	 * @return True if the ham was successfully submitted.
	 * @throws AkismetException
	 */
	public boolean submitHam(final AkismetComment comment) throws AkismetException {
		boolean rv = false;
		try {
			final HttpResponse response = this.callAkismet("submit-ham", comment);
			final String body = EntityUtils.toString(response.getEntity());
			if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				logger.warn(String.format("Something bad happened while submitting ham: %s", response.getStatusLine().getReasonPhrase()));
			else {
				logger.debug(String.format("Ham successfully submitted, response was '%s'", body));
				rv = true;
			}
		} catch(Exception e) {
			throw new AkismetException(e);
		}
		return rv;
	}
}
