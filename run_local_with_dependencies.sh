#!/usr/bin/env bash

sm -r --start ASSETS_FRONTEND IRR_DYNAMIC_STUB AFFINITY_GROUP AUTH AUTHENTICATOR AUTH_LOGIN_STUB AUTH_LOGIN_API USER_DETAILS IDENTITY_VERIFICATION CONTACT_FRONTEND

sbt -Dlogger.resource=logback-test.xml -Dplay.http.router=testOnlyDoNotUseInAppConf.Routes "run 9261"
