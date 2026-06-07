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

    // カメラ位置 → ワールドからカメラ基準へ
    point.x -= camera.position.x;
    point.y -= camera.position.y;
    point.z -= camera.position.z;

    Vec3 r = point;

    // カメラ回転の逆を適用（Y → X → Z の順）
    // yaw（左右）
    float cy = cos(-camera.rotation.y);
    float sy = sin(-camera.rotation.y);
    float x = r.x * cy + r.z * sy;
    float z = -r.x * sy + r.z * cy;
    r.x = x; r.z = z;

    // pitch（上下）
    float cx = cos(-camera.rotation.x);
    float sx = sin(-camera.rotation.x);
    float y = r.y * cx - r.z * sx;
    z = r.y * sx + r.z * cx;
    r.y = y; r.z = z;

    return r;
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
    // カメラ座標へ変換（位置 + 回転）
    Vec3 p = rotate(point, camera);

    if (p.z <= 0) return (Vec3){0,0,-1};

    float scale = tan(camera.fov * 0.5 * M_PI / 180.0f) * p.z;
    return (Vec3){(p.x / scale) * 100, (p.y / scale) * 100, p.z};
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

                                    // ❌ camera.position を足さない！！ 更新20251126
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
