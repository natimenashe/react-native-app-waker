'use strict'

import { NativeModules } from 'react-native'

export default class AppWaker {

    static doIt(){
        NativeModules.AppWaker.doIt();
    }
}
