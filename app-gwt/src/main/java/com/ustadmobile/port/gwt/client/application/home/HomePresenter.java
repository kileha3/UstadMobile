package com.ustadmobile.port.gwt.client.application.home;

import com.ustadmobile.port.gwt.client.application.ApplicationPresenter;
import com.ustadmobile.port.gwt.client.application.home.widget.ContactsWidgetPresenter;
import com.ustadmobile.port.gwt.client.place.NameTokens;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.Slot;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import com.ustadmobile.port.gwt.client.test.ReplaceWithThis;

/**
 * This is the child presenter of Application Presenter.
 * It uses its parent's presenter's (ApplicationPresenter) Slot to 
 *  reveal itself.
 *  
 *  extends Presenter<HomePresenter.MyView, ...  <--This defines the
 *  ApplicationPresenter superclass. Those interfaces need to be defined
 *  into the class.
 * @author varuna
 *
 */
public class HomePresenter 
extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy> {
	
	//Home Presenter's View 	
    interface MyView extends View {
    }

    //Home Presenter's ProxyPlace
    @NameToken(NameTokens.HOME)
    @ProxyStandard
    interface MyProxy extends ProxyPlace<HomePresenter> {
    }
    //A Presenter having a NameToken is a Place.

    //Place Manager
    private PlaceManager placeManager; 
    
    //We need to have a slot for PresenterWidget (Contacts):
    public static final Slot SLOT_CONTACTS = new Slot();
    
    //The PresenterWidget (Contacts)
    private ContactsWidgetPresenter contactsWidgetPresenter;
    
    @Inject
    HomePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy
            ,PlaceManager placeManager
            //,ContactsWidgetPresenter contactsWidgetPresenter
    		){
        super(eventBus, view, proxy, 
        		//ApplicationPresenter.SLOT_MAIN
        		ApplicationPresenter.SLOT_CONTENT
        		);
        this.placeManager = placeManager;
        
        //ContactsWidget PresenterWidget stuff:
        this.contactsWidgetPresenter = contactsWidgetPresenter;
        //Setting the PresenterWidget Contacts in its slot
        setInSlot(SLOT_CONTACTS, contactsWidgetPresenter);
        
        //If you have UiHandlers:
        //getView().setUiHandlers(this);
        
        //testing
        ReplaceWithThis bindingTest = new ReplaceWithThis();
        GWT.log("HomePresenter");
        GWT.log(bindingTest.value);
        
    }
}
