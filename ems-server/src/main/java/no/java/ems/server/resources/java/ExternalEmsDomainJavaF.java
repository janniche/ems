package no.java.ems.server.resources.java;

import fj.*;
import static fj.data.Option.*;
import no.java.ems.server.domain.*;

import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class ExternalEmsDomainJavaF {

    public static <A, B, L extends List<A>> ArrayList<B> mapArrayList(L as, F<A, B> f) {
        ArrayList<B> bs = new ArrayList<B>();
        for (A a : as) {
            bs.add(f.f(a));
        }
        return bs;
    }

    public static F<Event, no.java.ems.domain.Event> eventToExternal = new F<Event, no.java.ems.domain.Event>() {
        public no.java.ems.domain.Event f(Event event) {
            no.java.ems.domain.Event externalEvent = copy(event, new no.java.ems.domain.Event(event.getName()));
            externalEvent.setDate(event.getDate());
            externalEvent.setRooms(mapArrayList(event.getRooms(), roomToExternal));
            externalEvent.setDate(event.getDate());
            externalEvent.setTimeslots(event.getTimeslots());
            return externalEvent;
        }
    };

    public static F<EmailAddress, no.java.ems.domain.EmailAddress> emailAddressToExternal = new F<EmailAddress, no.java.ems.domain.EmailAddress>() {
        public no.java.ems.domain.EmailAddress f(EmailAddress emailAddress) {
            return new no.java.ems.domain.EmailAddress(emailAddress.getEmailAddress());
        }
    };

    public static F<Room, no.java.ems.domain.Room> roomToExternal = new F<Room, no.java.ems.domain.Room>() {
        public no.java.ems.domain.Room f(Room room) {
            no.java.ems.domain.Room externalRoom = copy(room, new no.java.ems.domain.Room(room.getName()));
            externalRoom.setDescription(room.getDescription());
            return externalRoom;
        }
    };

    public static F<Binary, no.java.ems.domain.Binary> binaryToExternal = new F<Binary, no.java.ems.domain.Binary>() {
        public no.java.ems.domain.Binary f(Binary binary) {
            if(binary instanceof UriBinary) {
                UriBinary uriBinary = (UriBinary) binary;

                return new no.java.ems.domain.UriBinary(binary.getId(), binary.getFileName(),
                        binary.getMimeType(), binary.getSize(), uriBinary.getUri());
            }
            else {
                throw new RuntimeException("Can't convert binaries (I'm lazy): " + binary.getClass());
            }
        }
    };

    public static F<Language, no.java.ems.domain.Language> languageToExternal = new F<Language, no.java.ems.domain.Language>() {
        public no.java.ems.domain.Language f(Language language) {
            return no.java.ems.domain.Language.valueOf(language.getIsoCode());
        }
    };

    public static F<Nationality, no.java.ems.domain.Nationality> nationalityToExternal = new F<Nationality, no.java.ems.domain.Nationality>() {
        public no.java.ems.domain.Nationality f(Nationality nationality) {
            return no.java.ems.domain.Nationality.valueOf(nationality.getIsoCode());
        }
    };
    
    public static <A extends AbstractEntity, B extends no.java.ems.domain.AbstractEntity> B copy(A a, B b) {
        b.setId(a.getId());
        b.setRevision(a.getRevision());
        b.setNotes(a.getNotes());
        b.setTags(a.getTags());
        b.setAttachements(mapArrayList(a.getAttachments(), binaryToExternal));
        return b;
    }

    public static F<Person, no.java.ems.domain.Person> personToExternal = new F<Person, no.java.ems.domain.Person>() {
        public no.java.ems.domain.Person f(Person person) {
            no.java.ems.domain.Person externalPerson = copy(person, new no.java.ems.domain.Person(person.getName()));
            externalPerson.setDescription(person.getDescription());
            externalPerson.setGender(no.java.ems.domain.Person.Gender.valueOf(person.getGender().name()));
            externalPerson.setBirthdate(person.getBirthdate());
            externalPerson.setLanguage(fromNull(person.getLanguage()).map(languageToExternal).orSome((no.java.ems.domain.Language) null));
            externalPerson.setNationality(fromNull(person.getNationality()).map(nationalityToExternal).orSome((no.java.ems.domain.Nationality)null));
            externalPerson.setEmailAddresses(mapArrayList(person.getEmailAddresses(), emailAddressToExternal));
            externalPerson.setPhoto(fromNull(person.getPhoto()).map(binaryToExternal).orSome((no.java.ems.domain.Binary) null));
            return externalPerson;
        }
    };
}
