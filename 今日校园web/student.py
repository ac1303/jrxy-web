class student:
    def __init__(self, studentID, psw, address_province,address_city,address_region,address,lon,lat,pushChannel,ServerChanKey,pushDeerKey,errorCount):
        self.studentID = studentID
        self.psw = psw
        self.address_province = address_province
        self.address_city = address_city
        self.address_region = address_region
        self.address = address
        self.lon = lon
        self.lat = lat
        self.pushChannel = pushChannel
        self.ServerChanKey = ServerChanKey
        self.pushDeerKey = pushDeerKey
        self.errorCount = errorCount
    
    def getStudentID(self):
        return self.studentID
    
    def setStudentID(self, studentID):
        self.studentID = studentID
    
    def getPsw(self):
        return self.psw
    
    def setPsw(self, psw):
        self.psw = psw

    def getAddress_province(self):
        return self.address_province
    
    def setAddress_province(self, address_province):
        self.address_province = address_province

    def getAddress_city(self):
        return self.address_city
    
    def setAddress_city(self, address_city):
        self.address_city = address_city
    
    def getAddress_region(self):
        return self.address_region
    
    def setAddress_region(self, address_region):
        self.address_region = address_region
    
    def getAddress(self):
        return self.address
    
    def setAddress(self, address):
        self.address = address
    
    def getLon(self):
        return self.lon
    
    def setLon(self, lon):
        self.lon = lon
    
    def getLat(self):
        return self.lat
    
    def setLat(self, lat):
        self.lat = lat

    def getPushChannel(self):
        return self.pushChannel
    
    def setPushChannel(self, pushChannel):
        self.pushChannel = pushChannel

    def getServerChanKey(self):
        return self.ServerChanKey
    
    def setServerChanKey(self, ServerChanKey):
        self.ServerChanKey = ServerChanKey
    
    def getPushDeerKey(self):
        return self.pushDeerKey
    
    def setPushDeerKey(self, pushDeerKey):
        self.pushDeerKey = pushDeerKey

    def getErrorCount(self):
        return self.errorCount

    def setErrorCount(self, errorCount):
        self.errorCount = errorCount
