extends Control
class_name Register
@onready var username_input = $Username
@onready var password_input = $Password
@onready var confirm_input = $ConfirmPassword

## Called when the node enters the scene tree.
## Sets metadata identifying the scene path for use with the SceneManager.
func _ready() -> void:
	# Assign the scene path metadata for SceneManager navigation
	set_meta("scene_path", "res://Myassets/Scenes/RegisterAndLogin/register.tscn")

## Called when the register button is pressed.
## Performs input validation and sends a registration request to the backend server.
func _on_button_register_pressed() -> void:
	# Clear previous errors (currently only logs to console)
	print("🔁 Attempting to register new user...")

	# Get trimmed input values from the text fields
	var username = username_input.text.strip_edges()
	var password = password_input.text.strip_edges()
	var confirm_password = confirm_input.text.strip_edges()

	# Basic validation: all fields must be filled
	if username.is_empty() or password.is_empty() or confirm_password.is_empty():
		print("❗ All fields are required.")
		return

	# Password confirmation validation
	if password != confirm_password:
		print("❗ Passwords do not match.")
		return

	# Create HTTPRequest node to perform web request
	var http = HTTPRequest.new()
	add_child(http)  # Important: must be added to the scene tree

	# Set maximum timeout for the request in seconds
	http.timeout = 5
	# Connect the signal to handle the server response
	http.request_completed.connect(_on_register_response)

	# Backend URL endpoint for user registration
	var url = PlayerInfo.urlSpring + "/auth/signup"

	# HTTP headers specifying JSON content type
	var headers = ["Content-Type: application/json"]

	# Prepare JSON body with username and password
	var request_body = JSON.stringify({
		"name": username,
		"password": password
	})

	# Send HTTP POST request to the server with the JSON data
	var err = http.request(url, headers, HTTPClient.METHOD_POST, request_body)
	if err != OK:
		print("❌ Failed to initiate HTTP request. Error code:", err)
		http.queue_free()

## Handles the server's response to the registration request.
##
## @param result Internal HTTPRequest result code (unused here).
## @param response_code HTTP status code from the server.
## @param headers Response headers.
## @param body Response body as bytes.
##
## On success (HTTP 200):
## - Prints confirmation.
## - Clears input fields.
## - Navigates back to the login screen.
##
## On failure:
## - Prints HTTP status and error details.
func _on_register_response(result: int, response_code: int, headers: PackedStringArray, body: PackedByteArray):
	# Convert response body bytes to string
	var response_text = body.get_string_from_utf8()
	print("📨 Server response:", response_code, response_text)

	# HTTP 200 = OK (registration successful)
	if response_code == 200:
		print("✅ User successfully registered!")

		# Clear input fields
		username_input.text = ""
		password_input.text = ""
		confirm_input.text = ""

		# Navigate back to the previous screen (login)
		SceneManager.go_back()
	else:
		# Registration failed: print status code and error message
		print("❌ Registration failed. HTTP code:", response_code)
		print("⚠️ Error details:", response_text)

## Called when the back button is pressed.
## Navigates back to the previous screen (usually the login screen).
func _on_button_back_pressed() -> void:
	SceneManager.go_back()
