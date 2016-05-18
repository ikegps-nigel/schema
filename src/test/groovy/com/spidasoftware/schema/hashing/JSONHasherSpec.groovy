package com.spidasoftware.schema.hashing

import groovy.json.JsonSlurper
import spock.lang.Specification

class JSONHasherSpec extends Specification{

	String json
	String jsonMixed

	def setup(){
		json = '{ "level": 1, "child": { "array": [1, 2, 3, 4, 5], "arrayOfObjects":[ {"name":"short", "age":12, "location":"someplace"}, {"name":"long", "age":66, "location":"nowhere"} ], "sibling":"sister" }, "otherProp":"something" }'
		jsonMixed = '{ "child": { "sibling": "sister", "array": [1, 3, 2, 5, 4], "arrayOfObjects": [{ "name": "long", "location": "nowhere", "age": 66 }, { "age": 12, "name": "short", "location": "someplace" }], }, "otherProp": "something", "level": 1 }'
	}

	def "Test hash produces a different value for objects with one different character"() {

		when:
			def jsonSlurper = new JsonSlurper()

			def jsonChanged = json.replace('"level": 1', '"level": 2')

			def map = jsonSlurper.parseText(json)
			def mapChanged = jsonSlurper.parseText(jsonChanged)

			def hash = JSONHasher.hash(json)
			def hashChanged = JSONHasher.hash(jsonChanged)

			def hashMap = JSONHasher.hash(map)
			def hashMapChanged = JSONHasher.hash(mapChanged)

		then:
			hash != hashChanged
			hashMap != hashMapChanged
			hash == hashMap
			hashChanged == hashMapChanged
			map.level==1
			mapChanged.level==2
	}

	def "hash should produce the same value for equal but mixed objects"() {

		when:
			def jsonSlurper = new JsonSlurper()

			def map = jsonSlurper.parseText(json) as Map
			def mapMixed = jsonSlurper.parseText(jsonMixed) as Map

			def hash = JSONHasher.hash(json)
			def hashMixed = JSONHasher.hash(jsonMixed)

			def hashMap = JSONHasher.hash(map)
			def hashMapMixed = JSONHasher.hash(mapMixed)


		then: "all the hashes should be equal"
			hash == hashMixed
			hashMap == hashMapMixed
			hashMap == hash

	}


	def "hashing should ignore the characters '_id', 'id', 'version'"() {

		when:
			def jsonSlurper = new JsonSlurper()

			def jsonId = json.replaceFirst(/\{/, '{"id": 1, ')
			def json_Id = json.replaceFirst(/\{/, '{"_id": 1, ')
			def jsonVersion = json.replaceFirst(/\{/, '{"version": "md5", ')

			def map = jsonSlurper.parseText(json)
			def mapId = jsonSlurper.parseText(jsonId)
			def map_Id = jsonSlurper.parseText(json_Id)
			def mapVersion = jsonSlurper.parseText(jsonVersion)

			def hash = JSONHasher.hash(json)
			def hashId = JSONHasher.hash(jsonId)
			def hash_Id = JSONHasher.hash(json_Id)
			def hashVersion = JSONHasher.hash(jsonVersion)

			def hashMap = JSONHasher.hash(map)
			def hashMapId = JSONHasher.hash(mapId)
			def hashMap_Id = JSONHasher.hash(map_Id)
			def hashMapVersion = JSONHasher.hash(mapVersion)

		then:
			hash == hashId
			hash == hash_Id
			hash == hashVersion
			hashMap == hashMapId
			hashMap == hashMap_Id
			hashMap == hashMapVersion

	}


}