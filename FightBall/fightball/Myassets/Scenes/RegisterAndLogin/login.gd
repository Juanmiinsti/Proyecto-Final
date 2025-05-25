extends Control
class_name Login

## Manages the login UI and process.
##
## This node handles user login by sending credentials to a backend REST API.
## On success, it receives an authentication token, fetches user information,
## and navigates to the main scene.
##
## It also provides navigation to the registration scene and supports
## resetting scene history when moving between scenes.

# HTTPRequest node used to send login and user info requests
var http_request: HTTPRequest

# Signal emitted when the login session is ready (custom signal)
signal session_ready

## Called when the node enters the scene tree.
## Sets metadata identifying the scene path.
## Initializes HTTPRequest node and connects the request completion signal.
func _ready() -> void:
	set_meta("scene_path", "res://Myassets/Scenes/RegisterAndLogin/Login.tscn")

	http_request = HTTPRequest.new()
	add_child(http_request)
	http_request.request_completed.connect(_on_request_completed)

## Called every frame (unused).
func _process(delta: float) -> void:
	pass

## Called when the button to return to the main scene is pressed.
## Resets scene navigation history and navigates to the main scene without adding it to history.
func _on_button_pressed() -> void:
	SceneManager.reset_history()
	SceneManager.go_to("res://Myassets/Scenes/MainScene/Mainscene.tscn", false)

## Called when the register button is pressed.
## Navigates to the registration scene.
func _on_button_register_pressed() -> void:
	SceneManager.go_to("res://Myassets/Scenes/RegisterAndLogin/register.tscn")

## Called when the login "accept" button is pressed.
## Validates username and password fields.
## If valid, sends an HTTP POST request with JSON body containing login credentials.
func _on_button_accept_pressed() -> void:
	var username = $Username.text
	var password = $Password.text

	if username.is_empty() or password.is_empty():
		print("Please fill in all fields.")
		return

	var login_data = {
		"name": username,
		"password": password
	}

	var json_body = JSON.stringify(login_data)
	var url = PlayerInfo.urlSpring + "/auth/login"

	var headers = ["Content-Type: application/json"]
	var result = http_request.request(url, headers, HTTPClient.METHOD_POST, json_body)

	if result != OK:
		print("Error initiating HTTP request.")

## Handles completion of the login HTTP request.
##
## @param result Internal HTTPRequest result code.
## @param response_code HTTP response code (200 = success).
## @param headers Response headers.
## @param body Response body bytes.
##
## On HTTP 200:
## - Extracts authentication token from response.
## - Stores username, password, and token in PlayerInfo.
## - Sends a follow-up request to fetch user details by username.
## - On success, stores user ID and saves user data persistently.
## - Navigates to the main scene and resets scene history.
##
## On failure:
## - Logs the error and response code.
func _on_request_completed(result, response_code, headers, body):
	if response_code == 200:
		var token = body.get_string_from_utf8()

		PlayerInfo.userName = $Username.text
		PlayerInfo.userPassword = $Password.text
		PlayerInfo.userKey = token

		print("✅ Login successful, token received.")
		print("🔍 Fetching user ID...")

		var user_info_request = HTTPRequest.new()
		add_child(user_info_request)
		user_info_request.timeout = 5

		user_info_request.request_completed.connect(
			func(_res, code, _headers, user_body):
				user_info_request.queue_free()
				
				if code == 200:
					var user_json = JSON.parse_string(user_body.get_string_from_utf8())
					PlayerInfo.userID = int(user_json["id"])
					print("✅ User ID:", PlayerInfo.userID)

					PlayerInfo.save_user_data()  # Persist user info

					SceneManager.reset_history()
					SceneManager.go_to("res://Myassets/Scenes/MainScene/Mainscene.tscn", false)
				else:
					print("❌ Error fetching user data:", code)
		)

		var url = PlayerInfo.urlSpring + "/api/userByName/%s" % PlayerInfo.userName
		var err = user_info_request.request(url, ["Accept: application/json"])

		if err != OK:
			print("❌ Error requesting user ID:", err)
	else:
		print("❌ Login failed. Response code:", response_code)
