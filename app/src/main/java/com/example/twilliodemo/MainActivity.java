package com.example.twilliodemo;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalDataTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoView;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import tvi.webrtc.Camera2Capturer;
import tvi.webrtc.Camera2Enumerator;
import tvi.webrtc.VideoCapturer;

public class MainActivity extends AppCompatActivity {

    boolean enableAudio = true;
    String frontCameraId = null;
    LocalAudioTrack localAudioTrack;
    LocalVideoTrack localVideoTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localAudioTrack = LocalAudioTrack.create(getApplicationContext(), enableAudio);
        Camera2Enumerator camera2Enumerator = new Camera2Enumerator(getApplicationContext());

        for (String cameraId : camera2Enumerator.getDeviceNames()) {
            if (camera2Enumerator.isFrontFacing(cameraId)) {
                frontCameraId = cameraId;
                break;
            }
        }

        if (frontCameraId != null) {

            VideoCapturer capturer = new Camera2Capturer(getApplicationContext(), frontCameraId, null);
            localVideoTrack = LocalVideoTrack.create(getApplicationContext(), enableAudio, capturer);

            VideoView videoView = findViewById(R.id.video_view);
            videoView.setMirror(true);
            videoView.setKeepScreenOn(true);

            assert localVideoTrack != null;
            localVideoTrack.addSink(videoView);

            /*assert localAudioTrack != null;
            localAudioTrack.release();

            localVideoTrack.release();*/
        }

        String token = TokenGenerator.generateToken();
        Log.e("Token==> ", token);

        connectToRoom("DailyStandup", token);
//        connectToRoom("DailyStandup", "eyJjdHkiOiJ0d2lsaW8tZnBhO3Y9MSIsInR5cCI6IkpXVCIsImFsZyI6IkhTMjU2In0.eyJpc3MiOiJTSzBkNmEyODI1NDQzYjYxMjEwMDMwZTRhODVmZGJjOTJhIiwiZXhwIjoxNjM1ODM2NTYwLCJncmFudHMiOnsiaWRlbnRpdHkiOiJ1c2VyQGV4YW1wbGUuY29tIiwidmlkZW8iOnsicm9vbSI6IkRhaWx5U3RhbmR1cCJ9fSwianRpIjoiU0swZDZhMjgyNTQ0M2I2MTIxMDAzMGU0YTg1ZmRiYzkyYS0xNjM1ODMyOTYwIiwic3ViIjoiQUM5OTI5MzM0YTQxNzcyMGY4NjcwNTE3NWNhYjg1NWM4OCJ9.2fHO8hwKcHVl-jQRS5kMA30vR1UDBQvlzGorywTdOm4");

    }

    public void connectToRoom(String roomName, String accessToken) {

        ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken)
                .roomName(roomName)
                .audioTracks(Collections.singletonList(localAudioTrack))
                .videoTracks(Collections.singletonList(localVideoTrack))
                .build();
        Video.connect(getApplicationContext(), connectOptions, roomListener());
    }

    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(@NonNull Room room) {
                Log.d("TAG", "Connected to " + room.getSid());

                LocalParticipant localParticipant = room.getLocalParticipant();
                Log.i("LocalParticipant ", localParticipant != null ? localParticipant.getIdentity() : null);

                List<RemoteParticipant> pp = room.getRemoteParticipants();
                //RemoteParticipant participant = room.getRemoteParticipants().get(0);
                if (pp.size() > 0) {
                    Log.i("HandleParticipants", pp.get(0).getIdentity() + " is in the room.");
                }
            }

            @Override
            public void onConnectFailure(@NonNull Room room, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onReconnecting(@NonNull Room room, @NonNull TwilioException twilioException) {

            }

            @Override
            public void onReconnected(@NonNull Room room) {

            }

            @Override
            public void onDisconnected(@NonNull Room room, @Nullable TwilioException twilioException) {

            }

            @Override
            public void onParticipantConnected(@NonNull Room room, @NonNull RemoteParticipant remoteParticipant) {
                Log.e("onParticipantConnected", room.toString());
            }

            @Override
            public void onParticipantDisconnected(@NonNull Room room, @NonNull RemoteParticipant remoteParticipant) {
                Log.i("Room.Listener", remoteParticipant.getIdentity() + " has left the room.");
            }

            @Override
            public void onRecordingStarted(@NonNull Room room) {

            }

            @Override
            public void onRecordingStopped(@NonNull Room room) {

            }
        };
    }
}