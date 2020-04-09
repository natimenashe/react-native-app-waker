'use strict'

import { NativeModules } from 'react-native'

export default class AppWaker {

    static setAlarm(id, timestamp, inexact){
        if(!(typeof id === 'string' || typeof id === 'number'))
            throw new Error('AppWaker.setAlarm: `id` must be a number or a string.')
        if(typeof timestamp !== 'number')
            throw new Error('AppWaker.setAlarm: `timestamp` must be an integer.')
        id = id.toString()  // parse to string
        timestamp = Math.trunc(timestamp)   // parse to int
        inexact = !!inexact // parse to bool
        NativeModules.AppWaker.setAlarm(id, timestamp, inexact)
    }

    static clearAlarm(id) {
        if(!(typeof id === 'string' || typeof id === 'number'))
            throw new Error('AppWaker.clearAlarm: `id` must be a number or a string.')
        id = id.toString()  // parse to string
        NativeModules.AppWaker.clearAlarm(id)
    }

    static isPermissionWindowNavigationNeeded() {
        return NativeModules.AppWaker.isPermissionWindowNavigationNeeded()


    }

    static async isPermissionWindowNavigationNeeded (){
            return await NativeModules.AppWaker.isPermissionWindowNavigationNeeded();
    }



    static navigateToPermissionsWindow() {
        return NativeModules.AppWaker.navigateToPermissionsWindow()
    }
}
