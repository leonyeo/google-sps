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

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        Collection<TimeRange> availableTimeRange = new ArrayList<TimeRange>();
        
        if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
            return availableTimeRange;
        }
        availableTimeRange.add(TimeRange.WHOLE_DAY);

        Collection<String> meetingAttendees = request.getAttendees();
        for (Event e : events) {
            Collection<String> eventAttendees = e.getAttendees();
            if (hasCommonAttendee (meetingAttendees, eventAttendees)) {
                TimeRange eventTime = e.getWhen();
                for (TimeRange t: new ArrayList<TimeRange>(availableTimeRange)) {
                    if (t.overlaps(eventTime)) {
                        if (t.contains(eventTime.start())) {
                            TimeRange newTimeslot = TimeRange.fromStartEnd(t.start(), eventTime.start(), false);
                            if (newTimeslot.duration() >= request.getDuration()) {
                                availableTimeRange.add(newTimeslot);
                            }
                        }
                        if (t.contains(eventTime.end())) {
                            TimeRange newTimeslot = TimeRange.fromStartEnd(eventTime.end(), t.end(), false);
                            if (newTimeslot.duration() >= request.getDuration()) {
                                availableTimeRange.add(newTimeslot);
                            }
                        }
                        availableTimeRange.remove(t);
                    }
                }
            }
        }

        return availableTimeRange;
    }

    private boolean hasCommonAttendee(Collection<String> meetingAttendees, Collection<String> eventAttendees) {
        return !Collections.disjoint(meetingAttendees, eventAttendees);
    }
}
