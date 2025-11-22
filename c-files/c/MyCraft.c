#include <windows.h>
#include <math.h>

#define M_PI 3.14159265358979323846

// 座標系
typedef struct {
    float x, y, z;
} Vec3;

// カメラ
typedef struct {
    Vec3 position;
    Vec3 rotation;  // 各軸に対する回転角度（ラジアン）
    float fov;      // カメラの視野角（Field of View）
    float speed;
} Camera;

// 立方体の頂点（1x1x1立方体の標準形）
Vec3 cubeVertices[8] = {
    {-0.5, -0.5, -0.5}, {0.5, -0.5, -0.5}, {0.5, 0.5, -0.5}, {-0.5, 0.5, -0.5},
    {-0.5, -0.5, 0.5},  {0.5, -0.5, 0.5},  {0.5, 0.5, 0.5},  {-0.5, 0.5, 0.5}
};

// ブロックサイズ（サイズを拡大）
const float BLOCK_SIZE = 1.0f;

// 3x3x3の座標系
int grid[3][3][3] = {
    {{1, 0, 1}, {0, 0, 0}, {0, 0, 0}},
    {{0, 1, 0}, {0, 0, 0}, {0, 0, 0}},
    {{1, 0, 1}, {0, 0, 0}, {0, 0, 0}}
};

// キーの状態を管理するフラグ（配列）
int keyFlags[256] = {0};

// 回転行列を適用して点を回転させる
Vec3 rotate(Vec3 point, Camera camera) {
    Vec3 rotated;
    Vec3 rotation = camera.rotation;
    Vec3 position = camera.position;
    point.x -= position.x;
    point.y -= position.y;
    point.z -= position.z;

    // x軸周りの回転
    rotated.y = point.y * cos(rotation.x) - point.z * sin(rotation.x);
    rotated.z = point.y * sin(rotation.x) + point.z * cos(rotation.x);
    rotated.x = point.x;

    // y軸周りの回転
    point = rotated;
    rotated.x = point.x * cos(rotation.y) + point.z * sin(rotation.y);
    rotated.z = -point.x * sin(rotation.y) + point.z * cos(rotation.y);
    rotated.y = point.y;

    // z軸周りの回転
    point = rotated;
    rotated.x = point.x * cos(rotation.z) - point.y * sin(rotation.z);
    rotated.y = point.x * sin(rotation.z) + point.y * cos(rotation.z);
    rotated.z = point.z;

    rotated.x += position.x;
    rotated.y += position.y;
    rotated.z += position.z;

    return rotated;
}

// プレイヤーの移動を更新する
void updataCameraMove(Camera* camera) {
    float forwardMove = 0.0f;
    float strafeMove = 0.0f;

    // 入力に基づいて前進と横移動の量を決定
    if (keyFlags['W']) {
        forwardMove += camera->speed;
    }
    if (keyFlags['S']) {
        forwardMove -= camera->speed;
    }
    if (keyFlags['A']) {
        strafeMove -= camera->speed;
    }
    if (keyFlags['D']) {
        strafeMove += camera->speed;
    }

    // プレイヤーの向きを考慮して移動ベクトルを計算
    float angle = camera->rotation.y;
    float cosAngle = cos(angle);
    float sinAngle = sin(angle);

    camera->position.x += forwardMove * cosAngle + strafeMove * sinAngle;
    camera->position.z += forwardMove * sinAngle - strafeMove * cosAngle;
}

// カメラの回転をプレイヤーの回転に合わせる
void updateCameraRotation(Camera* camera) {
    camera->rotation = camera->rotation;
    if (keyFlags[VK_LEFT]) {
        camera->rotation.y -= 0.05f;  // プレイヤーの左回転
    }
    if (keyFlags[VK_RIGHT]) {
        camera->rotation.y += 0.05f;  // プレイヤーの右回転
    }
    if (keyFlags[VK_UP]) {
        camera->rotation.x -= 0.05f;  // プレイヤーの上回転
    }
    if (keyFlags[VK_DOWN]) {
        camera->rotation.x += 0.05f;  // プレイヤーの下回転
    }
}

// カメラ視点からの投影（パースペクティブ投影）
Vec3 project(Vec3 point, Camera camera) {
    // カメラの回転を適用
    Vec3 rotatedPoint = rotate(point, camera);

    // カメラ位置を考慮し、視点からの座標に変換
    rotatedPoint.x -= camera.position.x;
    rotatedPoint.y -= camera.position.y;
    rotatedPoint.z -= camera.position.z;

    // 視野角によるパースペクティブ投影
    Vec3 projected;

    // カメラの手前にある点は描画しない
    if (rotatedPoint.z <= 0) {
        return (Vec3){0, 0, -1};  // z <= 0 の場合は描画しない
    }

    float scale = tan(camera.fov * 0.5 * M_PI / 180) * rotatedPoint.z;

    projected.x = (rotatedPoint.x / scale) * 100;  // スケール適用
    projected.y = (rotatedPoint.y / scale) * 100;

    return projected;
}

// ウィンドウプロシージャ: ウィンドウ内のイベント処理
LRESULT CALLBACK WindowProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam) {
    static Camera camera = {{0, 0, -5}, {0, 0, 0}, 90, 0.1f};  // カメラの初期設定
    PAINTSTRUCT ps;
    HDC hdc;
    static int drawFlag = 1;  // 1: 描画する（初期設定）

    switch (uMsg) {
        case WM_PAINT: {
            hdc = BeginPaint(hwnd, &ps);

            if (drawFlag) {
                RECT rect;
                GetClientRect(hwnd, &rect);
                HBRUSH blackBrush = CreateSolidBrush(RGB(0, 0, 0)); // 黒色ブラシ
                FillRect(hdc, &rect, blackBrush);  // 背景を黒色で塗りつぶす
                DeleteObject(blackBrush);  // ブラシを解放

                // 線を描く色を設定
                HPEN hPen = CreatePen(PS_SOLID, 2, RGB(0, 255, 0)); // 緑色の線
                SelectObject(hdc, hPen);

                // 3x3x3のグリッド上の立方体を描画
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        for (int z = 0; z < 3; z++) {
                            if (grid[x][y][z] == 1) {  // 立方体が存在する座標のみ描画
                                // 各頂点の座標をブロックサイズに合わせてスケーリング
                                POINT projectedPoints[8];
                                for (int i = 0; i < 8; i++) {
                                    Vec3 vertex = {
                                        cubeVertices[i].x * BLOCK_SIZE + x,
                                        cubeVertices[i].y * BLOCK_SIZE + y,
                                        cubeVertices[i].z * BLOCK_SIZE + z
                                    };

                                    // プレイヤー位置を考慮
                                    vertex.x += camera.position.x;
                                    vertex.y += camera.position.y;
                                    vertex.z += camera.position.z;

                                    Vec3 projected = project(vertex, camera);

                                    // zが-1のときは描画しない
                                    if (projected.z == -1) {
                                        continue;  // この頂点は描画しない
                                    }

                                    projectedPoints[i].x = (int)(projected.x + 250); // スクリーン中央に移動
                                    projectedPoints[i].y = (int)(projected.y + 250);
                                }

                                // 頂点をつないで立方体を描画
                                MoveToEx(hdc, projectedPoints[0].x, projectedPoints[0].y, NULL);
                                for (int i = 1; i < 4; i++) {
                                    LineTo(hdc, projectedPoints[i].x, projectedPoints[i].y);
                                }
                                LineTo(hdc, projectedPoints[0].x, projectedPoints[0].y);
                                for (int i = 4; i < 8; i++) {
                                    LineTo(hdc, projectedPoints[i].x, projectedPoints[i].y);
                                }
                                LineTo(hdc, projectedPoints[4].x, projectedPoints[4].y);

                                for (int i = 0; i < 4; i++) {
                                    MoveToEx(hdc, projectedPoints[i].x, projectedPoints[i].y, NULL);
                                    LineTo(hdc, projectedPoints[i + 4].x, projectedPoints[i + 4].y);
                                }
                            }
                        }
                    }
                }
            }

            EndPaint(hwnd, &ps);
            break;
        }

        case WM_KEYDOWN:
            keyFlags[wParam] = 1;  // キーが押された
            return 0;

        case WM_KEYUP:
            keyFlags[wParam] = 0;  // キーが離された
            return 0;

        case WM_DESTROY:
            PostQuitMessage(0); // ウィンドウが閉じられたときにアプリを終了
            return 0;

        case WM_TIMER:
            updataCameraMove(&camera);         // カメラの移動更新
            updateCameraRotation(&camera);    // カメラの回転更新
            InvalidateRect(hwnd, NULL, TRUE);  // ウィンドウを再描画
            return 0;
    }
    return DefWindowProc(hwnd, uMsg, wParam, lParam);
}

// WinMain: Windowsアプリケーションのエントリーポイント
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow) {
    const char CLASS_NAME[] = "CubeWindowClass";

    // ウィンドウクラスの設定
    WNDCLASS wc = { };
    wc.lpfnWndProc = WindowProc;              // ウィンドウプロシージャを指定
    wc.hInstance = hInstance;
    wc.lpszClassName = CLASS_NAME;

    // ウィンドウクラスを登録
    RegisterClass(&wc);

    // ウィンドウを作成
    int screen_w = GetSystemMetrics(SM_CXSCREEN);
    int screen_h = GetSystemMetrics(SM_CYSCREEN);
    HWND hwnd = CreateWindowEx(
        0,                                      // 拡張ウィンドウスタイル
        CLASS_NAME,                             // ウィンドウクラス
        "3D Cube Drawing",                      // ウィンドウタイトル
        WS_OVERLAPPEDWINDOW,                    // ウィンドウスタイル
        CW_USEDEFAULT, CW_USEDEFAULT, screen_w / 2, screen_h / 2, // ウィンドウの位置とサイズ
        NULL, NULL, hInstance, NULL);

    if (hwnd == NULL) {
        return 0;
    }

    // ウィンドウを表示
    ShowWindow(hwnd, nCmdShow);

    // タイマー設定（30FPS）
    SetTimer(hwnd, 1, 1000 / 30, NULL);

    // メッセージループ
    MSG msg;
    while (TRUE) {
        while (PeekMessage(&msg, NULL, 0, 0, PM_REMOVE)) {
            if (msg.message == WM_QUIT) {
                return 0;
            }
            TranslateMessage(&msg);
            DispatchMessage(&msg);
        }
    }
}
