package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.model.Tab;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class NavigationService {

    private Tab selectedTab;

    @PostConstruct
    public void postConstruct(){
        this.selectedTab = Tab.FILES;
    }

    public void setSelectedTab(Tab selectedTab) {
        this.selectedTab = selectedTab;
    }

    public Tab getSelectedTab() {
        return this.selectedTab;
    }
}
