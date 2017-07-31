package com.quad.xpress.webservice;

import com.quad.xpress.Myuploads.DeleteReq;
import com.quad.xpress.models.AlertStream.AlertStreamReq;
import com.quad.xpress.models.AlertStream.AlertStreamResp;
import com.quad.xpress.models.Follow.FollowRep;
import com.quad.xpress.models.Follow.FollowReq;
import com.quad.xpress.models.GetProfilePIc.GetPicReq;
import com.quad.xpress.models.GetProfilePIc.GetPicResp;
import com.quad.xpress.models.NotificationStream.NotificationStreamReq;
import com.quad.xpress.models.NotificationStream.NotificationStreamResp;
import com.quad.xpress.models.TrendingSearch.TsReq;
import com.quad.xpress.models.TrendingSearch.Tsresp;
import com.quad.xpress.models.abuse_resp.Abuse_Req;
import com.quad.xpress.models.abuse_resp.Abuse_response;
import com.quad.xpress.models.acceptRejectCount.AcceptRejectCount;
import com.quad.xpress.models.acceptRejectCount.ReqPvateCount;
import com.quad.xpress.models.authToken.AuthTokenReq;
import com.quad.xpress.models.authToken.AuthTokenResp;
import com.quad.xpress.models.clickResponce.Like_Req;
import com.quad.xpress.models.clickResponce.Like_Resp;
import com.quad.xpress.models.clickResponce.Viewed_Req;
import com.quad.xpress.models.contacts.ContactsReq;
import com.quad.xpress.models.contacts.ContactsResp;
import com.quad.xpress.models.counter.CounterReq;
import com.quad.xpress.models.counter.CounterResp;
import com.quad.xpress.models.featuredVideos.featuredReq;
import com.quad.xpress.models.featuredVideos.featuredResp;
import com.quad.xpress.models.otpverification.OTPMreq;
import com.quad.xpress.models.otpverification.OTPMresp;
import com.quad.xpress.models.otpverification.ResendOtpMreq;
import com.quad.xpress.models.otpverification.ResendOtpMresp;
import com.quad.xpress.models.privateAcceptReject.PrivARreq;
import com.quad.xpress.models.privateAcceptReject.PrivARresp;
import com.quad.xpress.models.privateBlock.PrivBlockReq;
import com.quad.xpress.models.profile_pic.profilepicResp;
import com.quad.xpress.models.receivedFiles.PlayListResp;
import com.quad.xpress.models.receivedFiles.Plist_Emotion.PlayListResp_emotion;
import com.quad.xpress.models.receivedFiles.PrivatePlayListReq;
import com.quad.xpress.models.receivedFiles.PublicPlayListReq;
import com.quad.xpress.models.receivedFiles.register.LanguageResp;
import com.quad.xpress.models.receivedFiles.register.ReqCL;
import com.quad.xpress.models.receivedFiles.register.RespCountry;
import com.quad.xpress.models.receivedFiles.search.SearchFilesReq;
import com.quad.xpress.models.registration.RegRequest;
import com.quad.xpress.models.registration.RegResp;
import com.quad.xpress.models.send.SVideoResp;
import com.quad.xpress.models.spam.spam_req;
import com.quad.xpress.models.spam.spamresp;
import com.quad.xpress.models.tagList.TagListReq;
import com.quad.xpress.models.tagList.TagListResp;
import com.quad.xpress.models.vp_guide.ReqGuide;
import com.quad.xpress.models.vp_guide.guide_status;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by Venkatesh on 12-04-16.
 */
public interface RxApi {

    @POST("/commandService/addDevice")
    void Registration(@Body RegRequest body, Callback<RegResp> callback);


    @POST("/commandService/OTPVerification")
    void OTPVerification(@Body OTPMreq body, Callback<OTPMresp> callback);


    @POST("/commandService/resendOTP")
    void ResendOTP(@Body ResendOtpMreq body, Callback<ResendOtpMresp> callback);


    @Multipart
    @POST("/commandService/videoFileUpload")
    void UploadVideo(@Header("authtoken") String authToken, @Part("fileupload") TypedFile file, @Part("from_email") String from_email,
                     @Part("to_email") String to_email, @Part("title") String title, @Part("tags") String tags,
                     @Part("privacy") String privacy, @Part("country") String country, @Part("language") String language, @Part("thumbnail") TypedFile thumbnail,
                     Callback<SVideoResp> callback);

    @Multipart
    @POST("/commandService/audioFileUpload")
    void UploadAudio(@Header("authtoken") String authToken, @Part("fileupload") TypedFile file, @Part("from_email") String from_email,
                     @Part("to_email") String to_email, @Part("title") String title, @Part("tags") String tags,
                     @Part("privacy") String privacy, @Part("country") String country, @Part("language") String language,
                     Callback<SVideoResp> callback);

    @POST("/queryService/myVideosToPlay")
    void MyPrivateLists(@Header("authtoken") String authToken, @Body PrivatePlayListReq body, Callback<PlayListResp> callback);

    @POST("/queryService/myVideosList")
    void Recent(@Header("authtoken") String authToken, @Body PublicPlayListReq body, Callback<PlayListResp_emotion> callback);

    @POST("/queryService/getAudioVideoListByLike")
    void MyPublicLists_Popular(@Header("authtoken") String authToken, @Body PublicPlayListReq body, Callback<PlayListResp_emotion> callback);
// void PublicLists_Emotion(@Header("authtoken") String authToken, @Body PublicPlayListReq body, Callback<PlayListResp_emotion> callback);

    @POST("/commandService/refreshToken")
    void RefreshTokenWS(@Body AuthTokenReq body, Callback<AuthTokenResp> callback);

    @POST("/commandService/feedbackAboutFile")
    void PrivateAcceptReject(@Header("authtoken") String authToken, @Body PrivARreq body, Callback<PrivARresp> callback);

    @POST("/commandService/blockUser")
    void PrivateBlockWS(@Header("authtoken") String authToken, @Body PrivBlockReq body, Callback<PrivARresp> callback);

    @POST("/queryService/getVideosByHashTags")
    void SearchFilesApi(@Header("authtoken") String authToken, @Body SearchFilesReq body, Callback<PlayListResp_emotion> callback);

    @POST("/commandService/emotionCount")
    void TagCloudApi(@Header("authtoken") String authToken, @Body TagListReq body, Callback<TagListResp> callback);

    @POST("/commandService/likeAudioVideo")
    void  Liked(@Header("authtoken") String authToken, @Body Like_Req body, Callback<Like_Resp> callback);

    @POST("/commandService/deleteAudioVideo")
            void  Delete(@Header("authtoken") String authToken, @Body DeleteReq body, Callback<Like_Resp> callback);

    @POST("/commandService/updateAudioView")
    void  Viewd(@Header("authtoken") String authToken, @Body Viewed_Req body, Callback<Like_Resp> callback);


    @POST("/commandService/audioVideoReportAbuse")
    void  Post_abuse(@Header("authtoken") String authToken, @Body Abuse_Req body, Callback<Abuse_response> callback);

    @POST("/queryService/getAudioVideoListByLike")
    void PublicLists_Emotion(@Header("authtoken") String authToken, @Body PublicPlayListReq body, Callback<PlayListResp_emotion> callback);

  //  /queryService/myUploads


    @POST("/queryService/ownuploads")
    void MyUploads_Private(@Header("authtoken") String authToken, @Body PublicPlayListReq body, Callback<PlayListResp_emotion> callback);

     @POST("/queryService/myUploads")
     void MyUploads_API(@Header("authtoken") String authToken, @Body PublicPlayListReq body, Callback<PlayListResp_emotion> callback);

    @POST("/queryService/getIsoList")
    void  GetCountry(@Header("authtoken") String authToken, @Body ReqCL body, Callback<RespCountry> callback);

    @POST("/queryService/getIsoList")
    void  GetLanguage(@Header("authtoken") String authToken, @Body ReqCL body, Callback<LanguageResp> callback);

    @POST("/queryService/getPrivateAcceptRejectCount")
    void  GetPvateCount(@Header("authtoken") String authToken, @Body ReqPvateCount body, Callback<AcceptRejectCount> callback);

    @POST("/queryService/promotions")
    void  GetGuide(@Header("authtoken") String authToken, @Body ReqGuide body, Callback<guide_status> callback);

    @POST("/commandService/getFeaturedVideoList")
    void  GetFeatured(@Header("authtoken") String authToken, @Body featuredReq body, Callback<featuredResp> callback);

    @POST("/queryService/getPopularVideosByHash")
    void  GetTrendingSearch(@Header("authtoken") String authToken, @Body featuredReq body, Callback<featuredResp> callback);


    @POST("/queryService/getPrivateFollowCount")
    void  CounterFeelings(@Header("authtoken") String authToken, @Body CounterReq body, Callback<CounterResp> callback);

    @POST("/commandService/getProfileImage")
    void  GetProfilePic(@Header("authtoken") String authToken, @Body GetPicReq body, Callback<GetPicResp> callback);

    @Multipart
    @POST("/commandService/profileImage")
    void  UploadProfilePic(@Header("authtoken") String authToken, @Part("profileImage") TypedFile file, @Part("user_email") String user_email, Callback<profilepicResp> callback);

    @POST("/commandService/followers")
    void  FollowRequest(@Header("authtoken") String authToken, @Body FollowReq body, Callback<FollowRep> callback);

    @POST("/commandService/unfollowers")
    void  UnFollowRequest(@Header("authtoken") String authToken, @Body FollowReq body, Callback<FollowRep> callback);

    @POST("/queryService/getFollowersVideos")
    void  NotificationStream(@Header("authtoken") String authToken, @Body NotificationStreamReq body, Callback<NotificationStreamResp> callback);

    @POST("/queryService/getPrivateFollowList")
    void  AlertStream(@Header("authtoken") String authToken, @Body AlertStreamReq body, Callback<AlertStreamResp> callback);

    @POST("/queryService/getIxpressContactList")
    void  PostContacts(@Header("authtoken") String authToken, @Body ContactsReq body, Callback<ContactsResp> callback);

    @POST("/queryService/getPopularVideosByHash")
    void  GetTrendingSearch(@Header("authtoken") String authToken, @Body TsReq body, Callback<Tsresp> callback);

    @POST("/commandService/spam")
    void  ReportSpam(@Header("authtoken") String authToken, @Body spam_req body, Callback<spamresp> callback);

    @POST("/commandService/del_status")
    void  DeletePrivate(@Header("authtoken") String authToken, @Body DeleteReq body, Callback<Like_Resp> callback);



    }