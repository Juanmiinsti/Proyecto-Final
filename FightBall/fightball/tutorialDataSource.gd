class_name TutorialDT
extends Node

## Manages loading and storing tutorials from a REST API.
##
## This node fetches the list of tutorials associated with the current user by making
## an HTTP GET request to the "/api/tutorials" endpoint. It parses the JSON response,
## converts it into tutorialDB instances, and stores them in DBtutorials.
##
## It avoids unnecessary reloads by comparing the hash of the JSON response.
##
## It listens for PlayerInfo.session_ready if the user session key is not ready
## when this node is initialized.

# Array to store all loaded tutorials
var DBtutorials: Array[tutorialDB] = []

# HTTPRequest node used to send the HTTP request for tutorials
var getAllTutorials: HTTPRequest

# Hash of the last JSON response to detect if data has changed
var data_hash: int = 0

# Flag to track if tutorials have been loaded at least once
var already_loaded: bool = false

## Called when the node enters the scene tree.
## Checks if the PlayerInfo.userKey (session token) is available.
## If not, connects to session_ready signal to wait for it.
## Otherwise, immediately triggers the loading of tutorials.
func _ready() -> void:
	if PlayerInfo.userKey.is_empty():
		PlayerInfo.session_ready.connect(_on_session_ready)
	else:
		_on_session_ready()

## Called when the user session is ready.
## Initializes the HTTPRequest node, sets up the completion signal,
## and sends an HTTP GET request to retrieve tutorials.
func _on_session_ready() -> void:
	print("🟢 PlayerInfo ready. Fetching tutorials...")

	getAllTutorials = HTTPRequest.new()
	add_child(getAllTutorials)

	getAllTutorials.request_completed.connect(_on_get_all_tutorials_completed)

	var headers = [
		"Accept: application/json",
		"Authorization: Bearer " + PlayerInfo.userKey
	]

	var result = getAllTutorials.request(
		PlayerInfo.urlSpring + "/api/tutorials",
		headers,
		HTTPClient.METHOD_GET
	)

	if result != OK:
		print("❌ Error starting HTTP request:", result)

## Handles the HTTPRequest completion signal.
##
## @param result Internal HTTPRequest result code.
## @param response_code HTTP response code (e.g., 200 for success).
## @param headers Response headers.
## @param body Response body bytes.
##
## On successful response (200):
## - Parses JSON from response body.
## - Compares JSON hash with previous to avoid redundant reload.
## - Clears and populates DBtutorials with new tutorialDB instances.
##
## Logs warnings or errors for empty/malformed data or HTTP errors.
func _on_get_all_tutorials_completed(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray) -> void:
	if response_code == 200:
		var json_string = body.get_string_from_utf8()
		var parsed_json = JSON.parse_string(json_string)
		var new_hash = hash(json_string)

		if already_loaded and new_hash == data_hash:
			print("🔁 Tutorials were already loaded and have not changed.")
			return

		DBtutorials.clear()
		if parsed_json is Array and parsed_json.size() > 0:
			for tut in parsed_json:
				var new_tutorial = parse_tutorial(tut)
				if new_tutorial:
					DBtutorials.append(new_tutorial)
			print("✅ Tutorials loaded:", DBtutorials.size())
		else:
			print("⚠️ Response is empty or malformed")

		data_hash = new_hash
		already_loaded = true
	else:
		print("❌ HTTP error", response_code)

## Parses an individual tutorial Dictionary into a tutorialDB instance.
##
## @param tut Dictionary with tutorial data from the JSON response.
## @return A tutorialDB instance with populated fields, or null if essential data missing.
func parse_tutorial(tut: Dictionary) -> tutorialDB:
	if not tut.has("title") or not tut.has("description"):
		print("⚠️ Incomplete tutorial data:", tut)
		return null

	var t = tutorialDB.new()
	t.title = tut["title"]
	t.description = tut["description"]
	return t
