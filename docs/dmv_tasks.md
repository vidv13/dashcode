Here are your pending manual tasks before the Play Store deploy can go live:

---

**Your To-Do (one-time setup, do at home):**

1. **Create Play Console listing** — sign into [play.google.com/console](https://play.google.com/console), create a new app with package name `com.vernonit.dashcode`
2. **Generate release keystore:**
   ```
   keytool -genkey -v -keystore dashcode.jks -alias dashcode -keyalg RSA -keysize 2048 -validity 10000
   ```
3. **Create Google service account** — in Play Console → Setup → API access, link to a Google Cloud project, create a service account with "Release manager" role, download the JSON key
4. **Add 5 GitHub secrets** in the repo Settings → Secrets → Actions:
   - `KEYSTORE_FILE` = `base64 -w 0 dashcode.jks`
   - `KEYSTORE_PASSWORD` = the password you set
   - `KEY_ALIAS` = `dashcode`
   - `KEY_PASSWORD` = the key password you set
   - `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` = contents of the downloaded JSON key file

---

Now back to the CI failure. I still can't read the raw logs, so I'm going to apply the broadest defensive opt-in fix: `ScalingLazyColumn` and `SwipeDismissableNavHost` may also require `@ExperimentalWearFoundationApi` in wear-compose-foundation 1.5.0. I'll add that alongside the Material3 opt-in in both affected files.