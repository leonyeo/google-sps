// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import java.util.Date;

// Class containing comment data
public final class Comment {
    private final long id;
    private final String name;
    private final Date timestamp;
    private final String text;
    private final String imageUrl;

    public Comment(long id, String name, Date timestamp, String text, String imageUrl) {
        this.id = id;
        this.name = name;
        this.timestamp = timestamp;
        this.text = text;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getText() {
        return this.text;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}