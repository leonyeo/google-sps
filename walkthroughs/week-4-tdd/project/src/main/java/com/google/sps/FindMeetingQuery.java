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
import java.util.Arrays;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        if (isMeetingRequestTooLong(request)) {
            return Collections.emptyList();
        }

        Collection<TimeRange> availableTimeRange = new ArrayList<TimeRange>();
        availableTimeRange.add(TimeRange.WHOLE_DAY);

        Collection<String> meetingAttendees = request.getAttendees();
        for (Event e : events) {
            Collection<String> eventAttendees = e.getAttendees();
            if (hasCommonAttendees (meetingAttendees, eventAttendees)) {
                TimeRange eventTime = e.getWhen();
                updateAvailableTimeRange(availableTimeRange, eventTime, request.getDuration());                
            }
        }

        return availableTimeRange;
    }

    private boolean hasCommonAttendees(Collection<String> meetingAttendees, Collection<String> eventAttendees) {
        return !Collections.disjoint(meetingAttendees, eventAttendees);
    }

    private boolean isMeetingRequestTooLong(MeetingRequest request) {
        return request.getDuration() > TimeRange.WHOLE_DAY.duration();
    }

    private boolean isTimeslotLongEnough(TimeRange timeslot, long meetingDuration) {
        return timeslot.duration() >= meetingDuration;
    }

    private void updateAvailableTimeRange(Collection<TimeRange> availableTimeRange, TimeRange eventTime, long meetingDuration) {
        for (TimeRange t: new ArrayList<TimeRange>(availableTimeRange)) {
            if (t.overlaps(eventTime)) {
                Collection<TimeRange> newTimeslots = splitTimeslot(t, eventTime);

                availableTimeRange.remove(t);

                for (TimeRange newTimeslot : newTimeslots) {
                    if (isTimeslotLongEnough (newTimeslot, meetingDuration)) {
                        availableTimeRange.add(newTimeslot);
                    }
                }
            }
        }
    }

    private Collection<TimeRange> splitTimeslot(TimeRange availableTimeslot, TimeRange eventTime) {
        Collection<TimeRange> newTimeslots = new ArrayList<TimeRange>();

        return Arrays.asList(
            TimeRange.fromStartEnd(availableTimeslot.start(), eventTime.start(), false),
            TimeRange.fromStartEnd(eventTime.end(), availableTimeslot.end(), false)
        );
    }
}
