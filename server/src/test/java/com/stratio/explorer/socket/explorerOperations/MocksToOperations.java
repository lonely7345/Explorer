/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.explorer.socket.explorerOperations;


import com.stratio.explorer.notebook.Note;
import com.stratio.explorer.notebook.Notebook;
import com.stratio.explorer.notebook.Paragraph;
import com.stratio.explorer.scheduler.Job;
import com.stratio.explorer.socket.Message;
import org.java_websocket.WebSocket;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;

public class MocksToOperations {


    static public Message mockMessage(String returValue){
        Message message = mock(Message.class);
        expect(message.get("id")).andStubReturn(returValue);
        replay(message);
        return message;
    }

    static public Notebook mockNotebook(boolean isCalled){
        Notebook noteBook = mock(Notebook.class);
        if (isCalled) {
            expect(noteBook.getNote(anyString())).andStubReturn(mockNote(isCalled));

        }
        replay(noteBook);
        return noteBook;
    }

    static public Note mockNote(boolean isCalled){
        Note note = mock(Note.class);
        if (isCalled) {
            expect(note.getParagraph(anyString())).andStubReturn(mockParagraph());

        }
        replay(note);
        return  note;

    }

    static public Paragraph mockParagraph(){
        Paragraph paragraph = mock(Paragraph.class);
        paragraph.setStatus(Job.Status.ABORT);
        expectLastCall().times(1);
        paragraph.setListener(null);
        expectLastCall().times(1);

        replay(paragraph);
        return paragraph;
    }

    static public WebSocket mockSocket(){
        WebSocket socket = mock(WebSocket.class);
        return socket;
    }



}
