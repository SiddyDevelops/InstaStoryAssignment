package com.siddydevelops.instastoryassignment.models

import com.google.android.exoplayer2.ExoPlayer

data class ExoPlayerItem(
    var exoPlayer: ExoPlayer,
    var position: Int
)